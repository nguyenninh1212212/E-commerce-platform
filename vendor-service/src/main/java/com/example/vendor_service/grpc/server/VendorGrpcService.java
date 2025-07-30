package com.example.vendor_service.grpc.server;

import com.example.vendor_service.model.dto.res.vendor.VendorBaseRes;
import com.example.vendor_service.service.VendorService;

import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import vendor.VendorResponse;
import vendor.VendorServiceGrpc.VendorServiceImplBase;

@GrpcService
@RequiredArgsConstructor
public class VendorGrpcService extends VendorServiceImplBase {
    private final VendorService service;

    @Override
    public void getVendor(vendor.VendorRequest request,
            io.grpc.stub.StreamObserver<vendor.VendorResponse> responseObserver) {
        try {
            VendorBaseRes baseRes = service.getVendor(request.getUserId());

            VendorResponse response = VendorResponse.newBuilder()
                    .setVendorId(baseRes.getVendorId().toString().toString())
                    .setVendorName(baseRes.getNameStore())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getVendorView(vendor.VendorRequest request,
            io.grpc.stub.StreamObserver<vendor.VendorResponse> responseObserver) {
        try {
            VendorBaseRes baseRes = service.getVendorView(request.getUserId());
            VendorResponse response = VendorResponse.newBuilder()
                    .setVendorId(baseRes.getVendorId().toString())
                    .setVendorName(baseRes.getNameStore())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

}
