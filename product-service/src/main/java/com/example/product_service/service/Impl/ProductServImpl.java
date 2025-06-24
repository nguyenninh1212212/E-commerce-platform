package com.example.product_service.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.product_service.grpc.client.CloudServiceGrpcClient;
import com.example.product_service.grpc.client.VariantServiceGrpcClient;
import com.example.product_service.mapper.ToModel;
import com.example.product_service.model.Attributes;
import com.example.product_service.model.Variants;
import com.example.product_service.model.dto.req.ProductReq;
import com.example.product_service.model.dto.res.Pagination;
import com.example.product_service.model.dto.res.ProductFeaturedRes;
import com.example.product_service.model.dto.res.ProductRes;
import com.example.product_service.model.entity.Product;
import com.example.product_service.model.enums.ProductStatus;
import com.example.product_service.model.enums.VariantsStatus;
import com.example.product_service.service.ProductService;

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

        @Override
        public String addProduct(ProductReq req, List<String> imgs) {
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
                                .inventory(req.getInventory())
                                .sellerId("abc123")
                                .imageUrl(imgs)
                                .build();
                mongoTemplate.save(product);
                return product.getId();
        }

        @Override
        public ProductRes getProductById(String id) {
                Product product = mongoTemplate.findById(id, Product.class);
                if (product == null) {
                        throw new IllegalArgumentException("Product not found with id: " + id);
                }
                List<VariantResponse> variantList = variantServiceGrpcClient.getVariantByProductId(id);
                List<Variants> variants = variantList != null ? ToModel.toVariantsRes(variantList, id)
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
                        cloudServiceGrpcClient.deleteImgs(product.getImageUrl());
                } catch (Exception e) {
                        log.info("delete product fail : ", e.getMessage());
                }
                mongoTemplate.remove(product);

        }

}
