package com.example.address_service.mapper;

import com.example.address_service.model.dto.req.AddressReq;
import com.example.address_service.model.dto.res.AddressRes;
import com.example.address_service.model.entity.Address;
import com.example.address_service.util.AuthenticationUtil;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ToModel {
    public Address toEntity(AddressReq req) {
        return Address.builder()
                .id(null)
                .userId(AuthenticationUtil.getSub())
                .fullName(req.getFullName())
                .phoneNumber(req.getPhoneNumber())
                .email(req.getEmail())
                .country(req.getCountry())
                .province(req.getProvince())
                .district(req.getDistrict())
                .ward(req.getWard())
                .street(req.getStreet())
                .addressLine2(req.getAddressLine2())
                .postalCode(req.getPostalCode())
                .isDefault(req.isDefault())
                .type(req.getType())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();
    }

    public AddressReq toReq(Address address) {
        return AddressReq.builder()
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .email(address.getEmail())
                .country(address.getCountry())
                .province(address.getProvince())
                .district(address.getDistrict())
                .ward(address.getWard())
                .street(address.getStreet())
                .addressLine2(address.getAddressLine2())
                .postalCode(address.getPostalCode())
                .isDefault(address.isDefault())
                .type(address.getType())
                .build();
    }

    public AddressRes toRes(Address address) {
        return AddressRes.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .email(address.getEmail())
                .country(address.getCountry())
                .province(address.getProvince())
                .district(address.getDistrict())
                .ward(address.getWard())
                .street(address.getStreet())
                .addressLine2(address.getAddressLine2())
                .postalCode(address.getPostalCode())
                .isDefault(address.isDefault())
                .type(address.getType())
                .build();
    }
}
