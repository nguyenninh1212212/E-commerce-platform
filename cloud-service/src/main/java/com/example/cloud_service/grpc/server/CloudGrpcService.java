package com.example.cloud_service.grpc.server;

import com.example.cloud_service.service.CloudServ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import cloud.CloudServiceGrpc;

@Slf4j
@RequiredArgsConstructor
@GrpcService
public class CloudGrpcService extends CloudServiceGrpc.CloudServiceImplBase {
    private final CloudServ cloudServ;

    @Override
    public void getMediaProduct(cloud.IdRequest request,
            io.grpc.stub.StreamObserver<cloud.CloudUrls> responseObserver) {
                
    }

    @Override
    public void getMediaAvatar(cloud.IdRequest request,
            io.grpc.stub.StreamObserver<cloud.CloudUrls> responseObserver) {
    }

}
