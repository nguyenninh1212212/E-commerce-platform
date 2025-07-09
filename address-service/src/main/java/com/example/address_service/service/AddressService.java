package com.example.address_service.service;

import java.util.List;

import com.example.address_service.model.dto.req.AddressReq;
import com.example.address_service.model.dto.res.AddressRes;

public interface AddressService {
    AddressRes create(AddressReq req);

    AddressRes getAddressById(String addressId);

    List<AddressRes> getByUser();

    void update(String addressId, AddressReq req);

    void delete(String addressId);
}