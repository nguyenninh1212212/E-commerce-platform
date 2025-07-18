package com.example.product_service.grpc.server;

import com.example.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import product.ProductServiceGrpc;
import product.SellerId;

@Slf4j
@RequiredArgsConstructor
@GrpcService
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {
    private final ProductService productService;

    @Override
    public void getSellerId(product.ProductId request,
            io.grpc.stub.StreamObserver<product.SellerId> responseObserver) {
        try {
            String sellerId = productService.getSellerId(request.getId());
            SellerId sellerProtoId = SellerId.newBuilder().setId(sellerId).build();
            responseObserver.onNext(sellerProtoId);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Loi khi ket noi grpc : ", e);

        }
    }

}
