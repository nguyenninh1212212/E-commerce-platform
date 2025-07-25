package com.example.variant_service.config.component;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.example.variant_service.grpc.client.ProductGrpcClient;
import com.example.variant_service.model.entity.Variant;
import com.example.variant_service.util.AuthenticationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import product.ProductId;
import product.ProductServiceGrpc.ProductServiceBlockingStub;

@Slf4j
@Component("checkIsOwner")
@RequiredArgsConstructor
public class CheckIsOwner {
    private final MongoTemplate mongoTemplatel;
    private final ProductGrpcClient productGrpcClient;

    public boolean IsSeller(String variantId) {

        String productId = mongoTemplatel.findOne(new Query(Criteria.where("id").is(variantId)), Variant.class)
                .getProductId();
        String sellerId = productGrpcClient.getSellerId(productId);
        if (productId == null) {
            throw new RuntimeException();
        }
        String userId = AuthenticationUtil.getSub();
        log.info("Check isSeller: sellerId={}, userId={}", sellerId, userId);
        return userId != null && userId.equals(sellerId);
    }

}
