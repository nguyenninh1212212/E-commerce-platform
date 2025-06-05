package com.example.product_service.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.product_service.grpc.client.VariantServiceGrpcClient;
import com.example.product_service.model.Attributes;
import com.example.product_service.model.Variants;
import com.example.product_service.model.dto.req.ProductReq;
import com.example.product_service.model.dto.req.ProductUpdateReq;
import com.example.product_service.model.dto.res.Pagination;
import com.example.product_service.model.dto.res.ProductFeaturedRes;
import com.example.product_service.model.dto.res.ProductRes;
import com.example.product_service.model.entity.Product;
import com.example.product_service.model.enums.VariantsStatus;
import com.example.product_service.service.CloudClientService;
import com.example.product_service.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import variant.VariantResponse;

@Service
@RequiredArgsConstructor
public class ProductServImpl implements ProductService {
        private final MongoTemplate mongoTemplate;
        private final VariantServiceGrpcClient variantServiceGrpcClient;
        private final CloudClientService cloudClientServ;

        @Override
        public void addProduct(ProductReq req, MultipartFile img) {
                String url = cloudClientServ.uploadToCloud(img, "image");
                Product product = Product.builder()
                                .name(req.getName())
                                .price(req.getPrice())
                                .description(req.getDescription())
                                .tag(req.getTags())
                                .reviewCount(0)
                                .rating(0)
                                .price(req.getPrice())
                                .attributes(req.getAttributes())
                                .sales(req.getSales())
                                .inventory(req.getInventory())
                                .sellerId("abc123")
                                .imageUrl(List.of(url))
                                .build();
                mongoTemplate.save(product);
        }

        @Override

        public ProductRes getProductById(String id) {
                Product product = mongoTemplate.findById(id, Product.class);
                if (product == null) {
                        throw new IllegalArgumentException("Product not found with id: " + id);
                }
                List<VariantResponse> variantList = variantServiceGrpcClient.getVariantByProductId(id);
                List<Variants> variants = variantList != null ? variantList.stream()
                                .map(variantResponse -> Variants.builder()
                                                .id(variantResponse.getId())
                                                .quantity(variantResponse.getQuantity())
                                                .productId(id)
                                                .attributes(variantResponse.getAttributesList().stream()
                                                                .map(attr -> Attributes.builder()
                                                                                .name(attr.getName())
                                                                                .value(attr.getValuesList())
                                                                                .build())
                                                                .toList())
                                                .status(VariantsStatus.valueOf(variantResponse.getStatus().name()))
                                                .price(variantResponse.getPrice())
                                                .build())
                                .toList() : new java.util.ArrayList<>();
                ProductRes res = ProductRes.builder()
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
                                .sellerId(id)
                                .build();
                return res;
        }

        private Pagination<List<ProductFeaturedRes>> getProductCriteria(Criteria criteria, int page, int limit) {
                Query query = new Query(criteria).skip(page * limit)
                                .limit(limit)
                                .with(Sort.by(Sort.Direction.ASC, "createdAt"));
                List<ProductFeaturedRes> products = mongoTemplate.find(query, Product.class).stream()
                                .map(product -> ProductFeaturedRes.builder()
                                                .id(product.getId())
                                                .name(product.getName())
                                                .sales(product.getSales())
                                                .price(product.getPrice())
                                                .image(product.getImageUrl().get(0))
                                                .build())
                                .toList();
                return Pagination.<List<ProductFeaturedRes>>builder().limit(20).page(0).result(products).build();
        }

        @Override

        public Pagination<List<ProductFeaturedRes>> getAllProducts(int page, int limit) {
                Criteria criteria = new Criteria();
                return getProductCriteria(criteria, page, limit);
        }

        @Override

        public Pagination<List<ProductFeaturedRes>> getProductsBySellerId(String sellerId) {
                Criteria criteria = Criteria.where("sellerId").is(sellerId);
                return getProductCriteria(criteria, 0, 20);
        }

        @Override

        public Pagination<List<ProductFeaturedRes>> getProductsByTag(String tag) {
                Criteria criteria = Criteria.where("tag").is(tag);
                return getProductCriteria(criteria, 0, 20);

        }

        @Override

        public void deleteProductById(String id) {
                Product product = mongoTemplate.findById(id, Product.class);
                if (product == null) {
                        throw new IllegalArgumentException("Product not found with id: " + id);
                }
                mongoTemplate.remove(product);
        }

        @Override

        public void updateProductById(String id, ProductUpdateReq req, MultipartFile img) {
                Product product = mongoTemplate.findById(id, Product.class);
                if (product == null) {
                        throw new IllegalArgumentException("Product not found with id: " + id);
                }
                if (img != null && !img.isEmpty()) {
                        for (String imgs : req.getImages()) {
                                if (req.getImages() != null && !req.getImages().isEmpty()) {
                                        cloudClientServ.deleteFromCloud(imgs);
                                        product.getImageUrl().clear();
                                }
                        }
                        String url = cloudClientServ.uploadToCloud(img, "image");
                        product.setImageUrl(List.of(url));
                }
                ObjectMapper objectMapper = new ObjectMapper();
                Update update = new Update();
                Map<String, Object> dtoMap = objectMapper.convertValue(req, new TypeReference<Map<String, Object>>() {
                });
                Map<String, Object> fieldFilter = dtoMap.entrySet().stream()
                                .filter(entry -> entry.getValue() != null)
                                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                fieldFilter.forEach(update::set);
                mongoTemplate.updateFirst(
                                Query.query(Criteria.where("id").is(id)), update, Product.class);
        }
}
