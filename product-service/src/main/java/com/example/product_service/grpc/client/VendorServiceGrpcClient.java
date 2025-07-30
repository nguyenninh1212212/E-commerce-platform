package com.example.product_service.grpc.client;

import org.springframework.stereotype.Component;

import com.example.product_service.model.dto.res.Vendor;
import com.example.product_service.util.AuthenticationUtil;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import vendor.VendorRequest;
import vendor.VendorResponse;
import vendor.VendorServiceGrpc.VendorServiceBlockingStub;

@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class VendorServiceGrpcClient {
    @GrpcClient("vendor-client")
    private VendorServiceBlockingStub blockingStub;

    private Vendor vendor(VendorResponse response) {
        Vendor vendor = new Vendor();
        vendor.setNameStore(response.getVendorName());
        vendor.setVendorId(response.getVendorId());
        return vendor;
    }

    public Vendor getVendor() {
        VendorRequest request = VendorRequest.newBuilder().setUserId(AuthenticationUtil.getSub()).build();
        VendorResponse response = blockingStub.getVendor(request);

        return vendor(response);
    }

    public Vendor getVendorView(String vendorId) {
        VendorRequest request = VendorRequest.newBuilder().setUserId(vendorId).build();
        VendorResponse response = blockingStub.getVendorView(request);
        return vendor(response);
    }
}
