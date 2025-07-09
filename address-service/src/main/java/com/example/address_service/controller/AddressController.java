package com.example.address_service.controller;

import com.example.address_service.model.dto.req.AddressReq;
import com.example.address_service.model.dto.res.AddressRes;
import com.example.address_service.service.AddressService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // Create address
    @PostMapping
    public ResponseEntity<AddressRes> createAddress(@RequestBody AddressReq req) {
        AddressRes res = addressService.create(req);
        return ResponseEntity.ok(res);
    }

    // Get all addresses by current user
    @GetMapping
    public ResponseEntity<List<AddressRes>> getAddressesByUser() {
        List<AddressRes> list = addressService.getByUser();
        return ResponseEntity.ok(list);
    }

    // Update address by addressId
    @PatchMapping("/{addressId}")
    public ResponseEntity<Void> updateAddress(
            @PathVariable String addressId,
            @RequestBody AddressReq req) {
        addressService.update(addressId, req);
        return ResponseEntity.noContent().build();
    }

    // Delete address by addressId
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String addressId) {
        addressService.delete(addressId);
        return ResponseEntity.noContent().build();
    }
}
