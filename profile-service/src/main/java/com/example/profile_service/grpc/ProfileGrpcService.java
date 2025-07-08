package com.example.profile_service.grpc;

import net.devh.boot.grpc.server.service.GrpcService;
import profile.ProfileServiceGrpc.ProfileServiceImplBase;

@GrpcService
public class ProfileGrpcService extends ProfileServiceImplBase {
    @Override
    public void getProfile(profile.UserId request,
            io.grpc.stub.StreamObserver<profile.ProfileResponse> responseObserver) {
    }

    @Override
    public void getGender(com.google.protobuf.Empty request,
            io.grpc.stub.StreamObserver<profile.Genders> responseObserver) {
    }

}
