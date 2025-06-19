package com.example.product_service.grpc.server;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.example.product_service.model.Attributes;
import com.example.product_service.model.Category;
import com.example.product_service.model.dto.req.ProductReq;
import com.example.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import product.ProductId;
import product.ProductServiceGrpc;

@RequiredArgsConstructor
@GrpcService
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {
        private final ProductService productService;

        @Override
        public void createProduct(product.ProductRequest request,
                        io.grpc.stub.StreamObserver<product.ProductId> responseObserver) {
                Category tags = Category.builder().name(request.getCategory().getName())
                                .id(request.getCategory().getId())
                                .build();
                List<Attributes> attributes = request.getAttributesList().stream().map(attb -> Attributes
                                .builder()
                                .name(attb.getName())
                                .value(attb.getValuesList()).build()).collect(Collectors.toList());
                ProductReq req = ProductReq.builder()
                                .sales(request.getSales())
                                .description(request.getDescription())
                                .name(request.getName())
                                .inventory(request.getInventory())
                                .sellerId(request.getSellerId())
                                .attributes(attributes)
                                .tags(tags)
                                .build();
                String productId = productService.addProduct(req, request.getImgList());
                product.ProductId id = ProductId.newBuilder().setId(productId).build();
                responseObserver.onNext(id);
                responseObserver.onCompleted();
        }

        @Override
        public void deleteProduct(product.ProductId request,
                        io.grpc.stub.StreamObserver<product.Empty> responseObserver) {
                productService.deleteProductById(request.getId());
                responseObserver.onNext(product.Empty.newBuilder().build());
                responseObserver.onCompleted();
        }
}
