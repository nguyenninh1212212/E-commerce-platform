package com.example.product_service.service;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.example.product_service.grpc.client.VariantServiceGrpcClient;
import com.example.product_service.model.Attributes;
import com.example.product_service.model.Variants;
import com.example.product_service.model.dto.req.ProductReq;
import com.example.product_service.model.dto.res.ProductRes;
import com.example.product_service.model.entity.Product;
import com.example.product_service.model.enums.VariantsStatus;

import lombok.RequiredArgsConstructor;
import variant.VariantResponse;

@Service
@RequiredArgsConstructor
public class ProductServ {
    private final MongoTemplate mongoTemplate;
    private final VariantServiceGrpcClient variantServiceGrpcClient;

    public void addProduct(ProductReq req) {
        Product product = Product.builder()
                .name(req.getName())
                .price(req.getPrice())
                .description(req.getDescription())
                .tag(req.getTags())
                .imageUrl(req.getImageUrl())
                .reviewCount(0)
                .rating(0)
                .discount(req.getPrice() - (req.getPrice() * req.getSales()) / 100)
                .price(req.getPrice())
                .attributes(req.getAttributes())
                .sales(0)
                .inventory(req.getInventory())
                .sellerId("abc123")
                .imageUrl(req.getImageUrl())
                .build();
        mongoTemplate.save(product);
    }

    public ProductRes getProductById(String id) {
        Product product = mongoTemplate.findById(id, Product.class);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        List<VariantResponse> variantList = variantServiceGrpcClient.getVariantByProductId(id);
        List<Variants> variants = variantList.stream().map(variantResponse -> Variants.builder()
                .id(variantResponse.getId())
                .quantity(variantResponse.getQuantity())
                .sku(variantResponse.getSku())
                .barcode(variantResponse.getBarcode())
                .productId(id)
                .attributes(variantResponse.getAttributesList().stream()
                        .map(attr -> Attributes.builder()
                                .name(attr.getName())
                                .value(attr.getValuesList())
                                .build())
                        .toList())
                .status(VariantsStatus.valueOf(variantResponse.getStatus().name()))
                .barcode(variantResponse.getBarcode())
                .imgurl(variantResponse.getImgurl())
                .price(variantResponse.getPrice())
                .build()).toList();
        ProductRes res = ProductRes.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .sales(product.getSales())
                .inventory(product.getInventory())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .attributes(product.getAttributes())
                .tags(product.getTag())
                .imageUrl(product.getImageUrl())
                .variants(variants)
                .build();
        return res;
    }

}
