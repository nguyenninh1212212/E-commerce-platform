package com.example.product_coordinator_service.grpc.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cloud.CloudRequest;
import cloud.CloudRequests;
import cloud.CloudUrl;
import cloud.CloudUrls;
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

    public List<String> getUrls(List<byte[]> fileBytes) {
        if (fileBytes == null || fileBytes.isEmpty()) {
            throw new IllegalArgumentException("❌ Danh sách file rỗng, không thể upload");
        }

        try {
            List<ByteString> byteStrings = fileBytes.stream()
                    .map(ByteString::copyFrom)
                    .toList();

            CloudRequests request = CloudRequests.newBuilder()
                    .setAssetFolder(assetFolder)
                    .setResourceType(resourceFile)
                    .addAllDatas(byteStrings)
                    .build();

            CloudUrls response = blockingStub.uploads(request);
            return response.getUrlList();

        } catch (Exception ex) {
            log.error("❌ Lỗi khi gọi gRPC Cloud service để upload ảnh: {}", ex.getMessage(), ex);
            throw new RuntimeException("Lỗi khi upload ảnh lên Cloud", ex);
        }
    }

}
