package com.example.product_service.grpc.client;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import cloud.CloudServiceGrpc;
import cloud.CloudUrl;

@Component
@NoArgsConstructor
public class CloudServiceGrpcClient {
    @GrpcClient("cloud-client")
    private CloudServiceGrpc.CloudServiceBlockingStub blockingStub;

    public void deleteImg(String url) {
        CloudUrl req = CloudUrl.newBuilder().setUrl(url).build();
        blockingStub.delete(req);
    }
}
