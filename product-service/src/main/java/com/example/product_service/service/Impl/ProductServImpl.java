package com.example.product_service.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.product_service.grpc.client.CloudServiceGrpcClient;
import com.example.product_service.grpc.client.VariantServiceGrpcClient;
import com.example.product_service.mapper.ToModel;
import com.example.product_service.model.Attributes;
import com.example.product_service.model.dto.res.VariantRes;
import com.example.product_service.model.dto.req.ProductReq;
import com.example.product_service.model.dto.req.VariantReq;
import com.example.product_service.model.dto.res.Pagination;
import com.example.product_service.model.dto.res.ProductFeaturedRes;
import com.example.product_service.model.dto.res.ProductRes;
import com.example.product_service.model.entity.Product;
import com.example.product_service.model.enums.ProductStatus;
import com.example.product_service.model.enums.VariantsStatus;
import com.example.product_service.service.ProductService;
import com.example.product_service.service.kafka.KafkaProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import variant.VariantResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServImpl implements ProductService {
        private final MongoTemplate mongoTemplate;
        private final VariantServiceGrpcClient variantServiceGrpcClient;
        private final CloudServiceGrpcClient cloudServiceGrpcClient;
        private final KafkaProducer kafkaProducer;

        @Override
        public void addProduct(ProductReq req, List<MultipartFile> imgs, List<VariantReq> variants) {
                List<byte[]> fileBytes = imgs.stream()
                                .map(file -> {
                                        try {
                                                return file.getBytes();
                                        } catch (Exception e) {
                                                log.error("Error reading file bytes: ", e);
                                                return null;
                                        }
                                })
                                .filter(bytes -> bytes != null)
                                .toList();

                List<String> imgUrls = cloudServiceGrpcClient.getUrls(fileBytes);
                Product product = Product.builder()
                                .name(req.getName())
                                .price(req.getPrice())
                                .description(req.getDescription())
                                .tag(req.getTags())
                                .status(ProductStatus.PENDING)
                                .reviewCount(0)
                                .rating(0)
                                .price(req.getPrice())
                                .attributes(req.getAttributes())
                                .sales(req.getSales())
                                .sellerId("abc123")
                                .imageUrl(imgUrls)
                                .build();
                mongoTemplate.save(product);
                kafkaProducer.sendEvent(product.getId(), variants);
        }

        @Override
        public ProductRes getProductById(String id) {
                Product product = mongoTemplate.findById(id, Product.class);
                if (product == null) {
                        throw new IllegalArgumentException("Product not found with id: " + id);
                }
                List<VariantResponse> variantList = variantServiceGrpcClient.getVariantByProductId(id);
                List<VariantRes> variants = variantList != null ? ToModel.toVariantsRes(variantList, id)
                                : new java.util.ArrayList<>();
                ProductRes res = ToModel.toRes(product, variants, "sellerId");
                return res;
        }

        private Pagination<List<ProductFeaturedRes>> getProductCriteria(Criteria criteria, int page, int limit) {
                Query query = new Query(criteria).skip(page * limit)
                                .limit(limit)
                                .with(Sort.by(Sort.Direction.ASC, "createdAt"));
                List<ProductFeaturedRes> products = mongoTemplate.find(query, Product.class).stream()
                                .map(ToModel::toFeatureRes)
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
                try {
                        kafkaProducer.sendMediaDeleteEvent(product.getImageUrl());
                        kafkaProducer.sendDeleteEvent(product.getId());
                } catch (Exception e) {
                        log.error("delete product fail : ", e.getMessage());
                }
                mongoTemplate.remove(product);

        }

}
