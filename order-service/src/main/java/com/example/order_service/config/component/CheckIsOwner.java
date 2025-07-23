package com.example.order_service.config.component;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.example.order_service.model.entity.Order;
import com.example.order_service.util.AuthenticationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("checkIsOwner")
@RequiredArgsConstructor
public class CheckIsOwner {
    private final MongoTemplate mongoTemplatel;
    private final StringRedisTemplate stringRedisTemplate;

    public boolean IsOwner(String orderId) {
        Order order = mongoTemplatel.findOne(new Query(Criteria.where("id").is(orderId)), Order.class);
        return order.getUserId().equals(AuthenticationUtil.getUserId());
    }

    public boolean IsSeller(String orderId) {

        String productId = mongoTemplatel.findOne(new Query(Criteria.where("id").is(orderId)), Order.class)
                .getProductPurchase().getProductId();
        String key = "ORDER_CACHE::" + productId;
        String rawSellerId = stringRedisTemplate.opsForValue().get(key);
        if (rawSellerId == null) {
            log.warn("Not found key Redis: {}", key);
            return false;
        }
        String sellerId = rawSellerId.replaceAll("^\"|\"$", "");
        String userId = AuthenticationUtil.getUserId();
        log.info("Check isSeller: key={}, sellerId={}, userId={}", key, sellerId, userId);
        return userId != null && userId.equals(sellerId);
    }

}
