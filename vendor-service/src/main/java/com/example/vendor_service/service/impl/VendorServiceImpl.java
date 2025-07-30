package com.example.vendor_service.service.impl;

import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.vendor_service.excep.AlreadyExist;
import com.example.vendor_service.excep.NotFound;
import com.example.vendor_service.model.dto.req.VendorReq;
import com.example.vendor_service.model.dto.res.vendor.VendorBaseRes;
import com.example.vendor_service.model.dto.res.vendor.VendorRes;
import com.example.vendor_service.model.entity.Vendor;
import com.example.vendor_service.repo.VendorRepo;
import com.example.vendor_service.service.VendorService;
import com.example.vendor_service.util.AuthenticationUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepo repo;
    private final ModelMapper mapper;

    private boolean checkExistingVendor() {
        return repo.findByUserId(AuthenticationUtil.getSub()) != null ? true : false;
    }

    @Override
    public boolean checkExistingNameStore(String nameStore) {
        int existing = repo.existStoreName(nameStore);
        if (existing > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void createVendor(VendorReq req) {
        if (checkExistingNameStore(req.getNameStore())) {
            throw new AlreadyExist("Name store");
        }
        if (!checkExistingVendor()) {
            Vendor vendor = mapper.map(req, Vendor.class);
            vendor.setUserId(AuthenticationUtil.getSub());
            repo.save(vendor);
        } else {
            throw new AlreadyExist("Vendor");

        }
    }

    @Override
    @Transactional
    public void updateVendor(VendorReq req) {
        Vendor vendor = repo.findByUserId(AuthenticationUtil.getSub());
        if (vendor == null) {
            new NotFound("Vendor");
        }
        mapper.map(req, vendor);
        repo.save(vendor);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void banVendor(UUID vendorId) {
        Vendor vendor = repo.findByVendorId(vendorId)
                .orElseThrow(() -> new NotFound("Vendor"));
        vendor.setBan(true);
        repo.save(vendor);
    }

    @Override
    public VendorBaseRes getVendor(String userId) {
        Vendor vendor = repo.findByUserId(userId);
        if (vendor == null)
            return null;

        return mapper.map(vendor, VendorBaseRes.class);
    }

    @Override
    public VendorBaseRes getVendorView(String vendorId) throws BadRequestException {
        UUID uuid;
        try {
            uuid = UUID.fromString(vendorId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format for vendorId: " + vendorId);
        }

        Vendor vendor = repo.findById(uuid).orElseThrow(() -> new NotFound("Vendor"));
        return mapper.map(vendor, VendorBaseRes.class);
    }

    @Override
    public VendorRes getVendor() {
        Vendor vendor = repo.findByUserId(AuthenticationUtil.getSub());
        return mapper.map(vendor, VendorRes.class);
    }

}
