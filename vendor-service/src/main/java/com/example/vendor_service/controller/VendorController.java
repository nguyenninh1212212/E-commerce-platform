package com.example.vendor_service.controller;

import com.example.vendor_service.model.dto.req.VendorReq;
import com.example.vendor_service.model.dto.res.ApiRes;
import com.example.vendor_service.model.dto.res.Message;
import com.example.vendor_service.model.dto.res.vendor.VendorBaseRes;
import com.example.vendor_service.service.VendorService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkExistingNameStore(@RequestParam String nameStore) {
        boolean exists = vendorService.checkExistingNameStore(nameStore);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiRes<VendorBaseRes>> getVendor(@PathVariable String userId) {
        VendorBaseRes vendor = vendorService.getVendor(userId);
        ApiRes<VendorBaseRes> res = ApiRes.<VendorBaseRes>builder()
                .status(HttpStatus.OK.value())
                .data(vendor)
                .build();
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<ApiRes<VendorBaseRes>> getTheVendor() {
        VendorBaseRes vendor = vendorService.getVendor();
        ApiRes<VendorBaseRes> res = ApiRes.<VendorBaseRes>builder()
                .status(HttpStatus.OK.value())
                .data(vendor)
                .build();
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<Message> createVendor(@RequestBody VendorReq req) {
        vendorService.createVendor(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Message.of(HttpStatus.CREATED.value(), "Vendor created successfully!"));
    }

    @PutMapping
    public ResponseEntity<Message> updateVendor(@RequestBody VendorReq req) {
        vendorService.updateVendor(req);
        return ResponseEntity
                .ok(Message.of(HttpStatus.OK.value(), "Vendor updated successfully!"));
    }

    @PutMapping("/ban")
    public ResponseEntity<Message> banVendor(@RequestParam UUID vendorId) {
        vendorService.banVendor(vendorId);
        return ResponseEntity
                .ok(Message.of(HttpStatus.OK.value(), "Vendor has been banned."));
    }

}
