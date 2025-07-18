package com.example.order_service.service;

import java.util.List;

import com.example.order_service.model.dto.req.OrderReq;
import com.example.order_service.model.dto.res.OrderRes;

public interface OrderService {

    List<OrderRes> getOrders();

    void createOrder(OrderReq req);

    void deleteOrder(String orderId);

    void confirmSale(String orderId, boolean isSell);

    void confirmDeliver(String orderId, boolean confirm);

}
