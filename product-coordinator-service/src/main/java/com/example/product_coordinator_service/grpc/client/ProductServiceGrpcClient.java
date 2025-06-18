package com.example.product_coordinator_service.grpc.client;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.product_coordinator_service.model.dto.req.Product;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import product.ProductServiceGrpc.ProductServiceBlockingStub;
import product.Category;
import product.ProductId;
import product.ProductRequest;
import shared.Attribute;

@Slf4j
@Component
@NoArgsConstructor
public class ProductServiceGrpcClient {

    @GrpcClient("product-client")
    private ProductServiceBlockingStub blockingStub;

    @Autowired
    private CloudServiceGrpcClient cloudServiceGrpcClient;

    public String CreateProduct(Product req, List<byte[]> img) {
        try {

            List<Attribute> attributes = req.getAttributes()
                    .stream()
                    .map(attb -> Attribute
                            .newBuilder()
                            .setName(attb.getName())
                            .addAllValues(attb.getValue())
                            .build())
                    .collect(Collectors.toList());

            List<String> imgAfterUpload = cloudServiceGrpcClient.getUrls(img);

            ProductRequest reqProto = ProductRequest
                    .newBuilder()
                    .addAllAttributes(attributes)
                    .setSales(req.getSales())
                    .addAllImg(imgAfterUpload)
                    .setCategory(
                            Category.newBuilder()
                                    .setId(req.getTags().getId())
                                    .setName(req.getTags().getName())
                                    .build())
                    .setName(req.getName())
                    .setInventory(req.getInventory())
                    .setDescription(req.getDescription())
                    .setSellerId(req.getSellerId())
                    .build();
            ProductId productId = blockingStub.createProduct(reqProto);
            return productId.getId();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void DeleteProduct(String productId) {
        try {
            ProductId req = ProductId.newBuilder().setId(productId).build();
            blockingStub.deleteProduct(req);
        } catch (Exception e) {
            log.info("Loi khi xoa product : ", e.getMessage());
            throw new RuntimeException(e.getMessage());

        }

    }
}
