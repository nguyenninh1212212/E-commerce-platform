package com.example.product_service.mapper;

import java.util.List;

import com.example.product_service.model.Attributes;
import com.example.product_service.model.Variants;
import com.example.product_service.model.dto.res.ProductFeaturedRes;
import com.example.product_service.model.dto.res.ProductRes;
import com.example.product_service.model.entity.Product;
import com.example.product_service.model.enums.VariantsStatus;

import lombok.experimental.UtilityClass;
import variant.VariantResponse;

@UtilityClass
public class ToModel {
        public ProductRes toRes(Product product, List<Variants> variants, String sellerId) {
                return ProductRes.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .discount(product.getPrice() - (product.getPrice() * product.getSales()) / 100)
                                .sales(product.getSales())
                                .inventory(product.getInventory())
                                .rating(product.getRating())
                                .reviewCount(product.getReviewCount())
                                .attributes(product.getAttributes())
                                .tags(product.getTag())
                                .imageUrl(product.getImageUrl())
                                .variants(variants)
                                .sellerId(sellerId)
                                .build();
        }

        public List<Variants> toVariantsRes(List<VariantResponse> variantList, String productId) {
                return variantList.stream()
                                .map(variantResponse -> Variants.builder()
                                                .id(variantResponse.getId())
                                                .quantity(variantResponse.getQuantity())
                                                .productId(productId)
                                                .attributes(variantResponse.getAttributesList().stream()
                                                                .map(attr -> Attributes.builder()
                                                                                .name(attr.getName())
                                                                                .value(attr.getValuesList())
                                                                                .build())
                                                                .toList())
                                                .status(VariantsStatus.valueOf(variantResponse.getStatus().name()))
                                                .price(variantResponse.getPrice())
                                                .build())
                                .toList();
        }

        public ProductFeaturedRes toFeatureRes(Product product) {
                return ProductFeaturedRes.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .sales(product.getSales())
                                .price(product.getPrice())
                                .image(product.getImageUrl().get(0))
                                .build();
        }
}
