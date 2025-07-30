package com.example.vendor_service.service;

import java.util.UUID;

import org.apache.coyote.BadRequestException;

import com.example.vendor_service.model.dto.req.VendorReq;
import com.example.vendor_service.model.dto.res.vendor.VendorBaseRes;
import com.example.vendor_service.model.dto.res.vendor.VendorRes;

public interface VendorService {
    boolean checkExistingNameStore(String nameStore);

    VendorBaseRes getVendor(String userId);

    void createVendor(VendorReq req);

    void updateVendor(VendorReq req);

    void banVendor(UUID vendorId);

    VendorBaseRes getVendorView(String userId) throws BadRequestException;

    VendorRes getVendor();

}
