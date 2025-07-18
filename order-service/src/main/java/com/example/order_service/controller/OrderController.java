package com.example.order_service.controller;

import com.example.order_service.model.dto.req.OrderReq;
import com.example.order_service.model.dto.res.OrderRes;
import com.example.order_service.model.enums.Status;
import com.example.order_service.service.OrderService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderRes>> getOrders() {
        return ResponseEntity.ok().body(orderService.getOrders());
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderReq req) {
        orderService.createOrder(req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/confirm-sale")
    public ResponseEntity<Void> confirmSale(
            @PathVariable String orderId,
            @RequestParam boolean isSell) {
        orderService.confirmSale(orderId, isSell);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/confirm-delivery")
    public ResponseEntity<Void> confirmDeliver(
            @PathVariable String orderId,
            @RequestParam boolean confirm) {
        orderService.confirmDeliver(orderId, confirm);
        return ResponseEntity.ok().build();
    }

}
