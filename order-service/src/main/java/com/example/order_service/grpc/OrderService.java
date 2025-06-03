package com.example.order_service.grpc;


import org.springframework.beans.factory.annotation.Autowired;

import com.example.order_service.service.OrderServ;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ordering.OrderRequest;
import ordering.OrderResponse;
import ordering.OrderServiceGrpc;

@GrpcService
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {

    @Autowired
    private OrderServ orderServ;

    @Override
    public void createOrder(OrderRequest request,
            StreamObserver<OrderResponse> responseObserver) {
        OrderResponse response = orderServ.createOrder(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
