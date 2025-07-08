package com.example.product_service.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.product_service.annotation.IsOwner;
import com.example.product_service.annotation.SellerOnly;
import com.example.product_service.exception.UnauthorizedException;
import com.example.product_service.exception.exceted.NotFoundException;
import com.example.product_service.grpc.client.VariantServiceGrpcClient;
import com.example.product_service.mapper.ToModel;
import com.example.product_service.model.dto.res.VariantRes;
import com.example.product_service.model.dto.req.product.ProductReq;
import com.example.product_service.model.dto.req.product.ProductUpdateReq;
import com.example.product_service.model.dto.req.variant.VariantReq;
import com.example.product_service.model.dto.res.Pagination;
import com.example.product_service.model.dto.res.ProductFeaturedRes;
import com.example.product_service.model.dto.res.ProductRes;
import com.example.product_service.model.entity.Product;
import com.example.product_service.model.enums.ProductStatus;
import com.example.product_service.service.ProductService;
import com.example.product_service.service.kafka.KafkaProducer;
import com.example.product_service.util.AuthenticationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import variant.VariantResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServImpl implements ProductService {
        private final MongoTemplate mongoTemplate;
        private final VariantServiceGrpcClient variantServiceGrpcClient;
        private final KafkaProducer kafkaProducer;
        private final ObjectMapper objectMapper;

        @SellerOnly
        @Override
        public String addProduct(ProductReq req) {
                Product product = Product.builder()
                                .name(req.getName())
                                .price(req.getPrice())
                                .description(req.getDescription())
                                .tag(req.getTags())
                                .status(ProductStatus.PENDING)
                                .reviewCount(0)
                                .rating(0)
                                .attributes(req.getAttributes())
                                .sales(req.getSales())
                                .sellerId(AuthenticationUtil.getSub())
                                .build();
                mongoTemplate.save(product);

                try {
                        kafkaProducer.sendEvent(product.getId(), req.getVariants());
                } catch (Exception e) {
                        throw new RuntimeException("Failed to process product creation", e);
                }
                return product.getId();
        }

        @Override
        public ProductRes getProductById(String id) {
                Product product = findProductOrThrow(id);
                List<VariantResponse> variantList = variantServiceGrpcClient.getVariantByProductId(id);
                List<VariantRes> variants = variantList != null ? ToModel.toVariantsRes(variantList, id)
                                : new java.util.ArrayList<>();
                ProductRes res = ToModel.toRes(product, variants, product.getSellerId());
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

        @SellerOnly
        @IsOwner
        @Override
        public void deleteProductById(String id) {
                Product product = findProductOrThrow(id);
                try {
                        kafkaProducer.sendDeleteEvent(product.getId());
                } catch (Exception e) {
                        log.error("delete product fail : ", e.getMessage());
                }
                mongoTemplate.remove(product);

        }

        @SellerOnly
        @IsOwner
        public void updateProduct(String id, ProductUpdateReq updates) {
                Query query = new Query(Criteria.where("id").is(id));
                Update update = new Update();

                Map<String, Object> map = objectMapper.convertValue(updates, Map.class);

                map.entrySet().stream()
                                .filter(e -> e.getValue() != null)
                                .filter(e -> !e.getKey().equals("sellerId"))
                                .forEach(e -> update.set(e.getKey(), e.getValue()));

                mongoTemplate.updateFirst(query, update, Product.class);
        }

        private Product findProductOrThrow(String id) {
                Product product = mongoTemplate.findById(id, Product.class);
                if (product == null) {
                        throw new NotFoundException("Product");
                }
                return product;
        }

}
