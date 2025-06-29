package com.example.product_service.grpc.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import cloud.CloudRequests;
import cloud.CloudServiceGrpc;
import cloud.CloudUrl;
import cloud.CloudUrls;

@Slf4j
@Component
@NoArgsConstructor
public class CloudServiceGrpcClient {
    @GrpcClient("cloud-client")
    private CloudServiceGrpc.CloudServiceBlockingStub blockingStub;
    @Value("${cloudinary.resourceFile}")
    private String resourceFile;
    @Value("${cloudinary.assetFolder}")
    private String assetFolder;

    public void deleteImgs(List<String> url) {
        CloudUrls req = CloudUrls.newBuilder().addAllUrl(url).build();
        blockingStub.deletes(req);
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
