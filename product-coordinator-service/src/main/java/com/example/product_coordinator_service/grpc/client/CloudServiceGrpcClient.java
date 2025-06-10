package com.example.product_coordinator_service.grpc.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cloud.CloudRequest;
import cloud.CloudUrl;
import cloud.CloudServiceGrpc.CloudServiceBlockingStub;
import com.google.protobuf.ByteString;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Slf4j
@Component
@NoArgsConstructor
public class CloudServiceGrpcClient {
    @GrpcClient("cloud-client")
    private CloudServiceBlockingStub blockingStub;
    @Value("${cloudinary.resourceFile}")
    private String resourceFile;
    @Value("${cloudinary.assetFolder}")
    private String assetFolder;

    public String getUrl(byte[] fileBytes) {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new IllegalArgumentException("❌ File truyền vào rỗng");
        }
        try {
            CloudRequest req = CloudRequest.newBuilder()
                    .setAssetFolder(assetFolder)
                    .setResourceType(resourceFile)
                    .setData(ByteString.copyFrom(fileBytes))
                    .build();
            CloudUrl url = blockingStub.upload(req);
            return url.getUrl();
        } catch (Exception e) {
            log.error("❌ Loi goi gRPC cloud: {}", e.getMessage(), e);
            return null;
        }
    }
}
