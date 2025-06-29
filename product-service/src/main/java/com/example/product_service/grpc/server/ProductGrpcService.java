// package com.example.product_service.grpc.server;

// import java.util.List;
// import java.util.NoSuchElementException;
// import java.util.stream.Collector;
// import java.util.stream.Collectors;

// import com.example.product_service.model.Attributes;
// import com.example.product_service.model.Category;
// import com.example.product_service.model.dto.req.ProductReq;
// import com.example.product_service.service.ProductService;

// import lombok.RequiredArgsConstructor;
// import net.devh.boot.grpc.server.service.GrpcService;
// import product.ProductId;
// import product.ProductServiceGrpc;

// @RequiredArgsConstructor
// @GrpcService
// public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {
//         private final ProductService productService;


//         @Override
//         public void deleteProduct(product.ProductId request,
//                         io.grpc.stub.StreamObserver<product.Empty> responseObserver) {
//                 try {
//                         productService.deleteProductById(request.getId());
//                         responseObserver.onNext(product.Empty.newBuilder().build());
//                         responseObserver.onCompleted();
//                 } catch (NoSuchElementException e) {
//                         responseObserver.onError(io.grpc.Status.NOT_FOUND
//                                         .withDescription("Product not found with ID: " + request.getId())
//                                         .asRuntimeException());
//                 } catch (Exception e) {
//                         responseObserver.onError(io.grpc.Status.INTERNAL
//                                         .withDescription("Failed to delete product")
//                                         .augmentDescription(e.getMessage())
//                                         .asRuntimeException());
//                 }
//         }

// }
