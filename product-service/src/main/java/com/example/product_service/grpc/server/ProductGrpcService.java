package com.example.product_service.grpc.server;

import java.util.List;
import java.util.NoSuchElementException;
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
                try {
                        // Build category
                        Category tags = Category.builder()
                                        .name(request.getCategory().getName())
                                        .id(request.getCategory().getId())
                                        .build();

                        // Build attributes
                        List<Attributes> attributes = request.getAttributesList().stream()
                                        .map(attb -> Attributes.builder()
                                                        .name(attb.getName())
                                                        .value(attb.getValuesList())
                                                        .build())
                                        .collect(Collectors.toList());

                        // Build product request
                        ProductReq req = ProductReq.builder()
                                        .sales(request.getSales())
                                        .description(request.getDescription())
                                        .name(request.getName())
                                        .inventory(request.getInventory())
                                        .sellerId(request.getSellerId())
                                        .attributes(attributes)
                                        .tags(tags)
                                        .build();

                        // Gọi service thực thi
                        String productId = productService.addProduct(req, request.getImgList());

                        // Trả kết quả cho client
                        product.ProductId id = ProductId.newBuilder().setId(productId).build();
                        responseObserver.onNext(id);
                        responseObserver.onCompleted();

                } catch (IllegalArgumentException e) {
                        responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                                        .withDescription("Invalid input: " + e.getMessage())
                                        .asRuntimeException());
                } catch (Exception e) {
                        e.printStackTrace(); // hoặc log.error(...)
                        responseObserver.onError(io.grpc.Status.INTERNAL
                                        .withDescription("Internal error occurred while creating product")
                                        .augmentDescription(e.getMessage())
                                        .asRuntimeException());
                }
        }

        @Override
        public void deleteProduct(product.ProductId request,
                        io.grpc.stub.StreamObserver<product.Empty> responseObserver) {
                try {
                        productService.deleteProductById(request.getId());
                        responseObserver.onNext(product.Empty.newBuilder().build());
                        responseObserver.onCompleted();
                } catch (NoSuchElementException e) {
                        responseObserver.onError(io.grpc.Status.NOT_FOUND
                                        .withDescription("Product not found with ID: " + request.getId())
                                        .asRuntimeException());
                } catch (Exception e) {
                        responseObserver.onError(io.grpc.Status.INTERNAL
                                        .withDescription("Failed to delete product")
                                        .augmentDescription(e.getMessage())
                                        .asRuntimeException());
                }
        }

}
