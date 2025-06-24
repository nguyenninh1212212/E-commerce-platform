package com.example.cloud_service.grpc.server;

import java.util.List;
import java.util.stream.Collectors;

import com.example.cloud_service.cloud.CloudServ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import cloud.CloudServiceGrpc;
import cloud.CloudUrl;
import cloud.CloudUrls;
import cloud.Messages;

@Slf4j
@RequiredArgsConstructor
@GrpcService
public class CloudGrpcService extends CloudServiceGrpc.CloudServiceImplBase {
    private final CloudServ cloudServ;

    @Override
    public void upload(cloud.CloudRequest request,
            io.grpc.stub.StreamObserver<cloud.CloudUrl> responseObserver) {
        try {
            byte[] data = request.getData().toByteArray();
            String asset_folder = request.getAssetFolder();
            String type = request.getResourceType();
            String url = cloudServ.upload(data, type, asset_folder);
            CloudUrl cloudResponse = CloudUrl.newBuilder().setUrl(url).build();
            responseObserver.onNext(cloudResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("upload error : ", e.getMessage(), e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void uploads(cloud.CloudRequests request,
            io.grpc.stub.StreamObserver<cloud.CloudUrls> responseObserver) {
        try {
            List<byte[]> datas = request.getDatasList().stream()
                    .map(fb -> fb.toByteArray()).toList();
            String asset_folder = request.getAssetFolder();
            String type = request.getResourceType();
            List<String> url = cloudServ.uploads(datas, type, asset_folder);
            CloudUrls cloudResponse = CloudUrls.newBuilder().addAllUrl(url).build();
            responseObserver.onNext(cloudResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("upload error : ", e.getMessage(), e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void delete(cloud.CloudUrl request,
            io.grpc.stub.StreamObserver<cloud.Messages> responseObserver) {
        try {
            cloudServ.deleteFileByUrl(request.getUrl());
            Messages res = Messages.newBuilder().setValue("Delete media successfully!!").build();
            responseObserver.onNext(res);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("delete error : ", e.getMessage());
            responseObserver.onError(e);

        }
    }

    @Override
    public void deletes(cloud.CloudUrls request,
            io.grpc.stub.StreamObserver<cloud.Messages> responseObserver) {
        try {
            cloudServ.deleteListFile(request.getUrlList());
            Messages res = Messages.newBuilder().setValue("Delete medias successfully!!").build();
            responseObserver.onNext(res);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("Delete list file error : ", e.getMessage());
            responseObserver.onError(e);
        }
    }

}
