package com.example.order_service.service.impl;

import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.order_service.annotation.IsOrderOwner;
import com.example.order_service.annotation.IsSeller;
import com.example.order_service.excep.AlreadyExist;
import com.example.order_service.excep.NotAllowed;
import com.example.order_service.grpc.client.ProductGrpcClient;
import com.example.order_service.mapper.ToModel;
import com.example.order_service.model.dto.MessageSocketDTO;
import com.example.order_service.model.dto.req.OrderReq;
import com.example.order_service.model.dto.res.OrderRes;
import com.example.order_service.model.entity.Order;
import com.example.order_service.model.entity.PaymentMethod;
import com.example.order_service.model.enums.Status;
import com.example.order_service.service.OrderService;
import com.example.order_service.service.kafka.KafkaProducer;
import com.example.order_service.util.AuthenticationUtil;
import com.mongodb.client.result.UpdateResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServImpl implements OrderService {
    private final MongoTemplate mongoTemplate;
    private final CacheManager cacheManager;
    private final ProductGrpcClient productGrpcClient;
    private final KafkaProducer kafkaProducer;

    @PreAuthorize("hasRole('USER')")
    @Override
    public void createOrder(OrderReq req) {
        if (mongoTemplate.findOne(new Query(Criteria.where("type").is(req.getPayment_method())),
                PaymentMethod.class).isUseable() == false) {
            throw new NotAllowed("Payment method");
        }
        Order order = ToModel.toEntity(req);
        Cache cache = cacheManager.getCache("ORDER_CACHE");
        String productID = order.getProductPurchase().getProductId();
        String sellerId = productGrpcClient.getSellerId(productID);
        if (sellerId.equals(AuthenticationUtil.getUserId())) {
            throw new AlreadyExist("You cannot buy this products because you are seller this product!!");
        }
        cache.put(productID, productGrpcClient.getSellerId(productID));
        kafkaProducer.sendStockReverse(req.getProductPurchase().getVariantPurchases().getVariantId(),
                req.getQuantity());

        mongoTemplate.save(order);
    }

    @IsOrderOwner
    @Override
    public void deleteOrder(String orderId) {
        Query query = new Query(Criteria.where("id").is(orderId));
        mongoTemplate.findAndRemove(query, Order.class);
    }

    @IsSeller
    @Override
    public void confirmSale(String orderId, boolean isSell) {
        Query query = Query.query(Criteria.where("id").is(orderId));
        Status status = isSell ? Status.PROCESSING : Status.CANCELLED;
        Update update = Update.update("status", status);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Order.class);
        if (result.getModifiedCount() == 0) {
            log.warn("No order updated with id: {}", orderId);
            return;
        }
        MessageSocketDTO messageSocket = buildOrderNotification(orderId, isSell);
        kafkaProducer.sendCustomerNotification(messageSocket, orderId);
    }

    private MessageSocketDTO buildOrderNotification(String orderId, boolean isSell) {
        String statusText = isSell ? "was confirm" : "was cancel";
        String title = "The order " + statusText;
        return MessageSocketDTO.builder()
                .message(orderId)
                .title(title)
                .type("ORDER")
                .build();
    }

    @Override
    public void confirmDeliver(String orderId, boolean confirm) {
        throw new UnsupportedOperationException("Unimplemented method 'confirmDeliver'");
    }

    @Override
    public List<OrderRes> getOrders() {
        Query query = new Query(Criteria.where("userId").is(AuthenticationUtil.getUserId()));
        List<OrderRes> orderReses = mongoTemplate.find(query, Order.class).stream()
                .map(ToModel::toRes).toList();
        return orderReses;

    }

}
