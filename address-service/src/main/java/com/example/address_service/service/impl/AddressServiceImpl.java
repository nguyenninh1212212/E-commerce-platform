package com.example.address_service.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.address_service.mapper.ToModel;
import com.example.address_service.model.dto.req.AddressReq;
import com.example.address_service.model.dto.res.AddressRes;
import com.example.address_service.model.entity.Address;
import com.example.address_service.service.AddressService;
import com.example.address_service.util.AuthenticationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final MongoTemplate mongoTemplate;
    private static String userId = AuthenticationUtil.getSub();

    @Override
    public AddressRes create(AddressReq req) {

        long count = mongoTemplate.count(new Query(Criteria.where("userId").is(userId)),
                Address.class);
        if (count >= 3) {
            throw new IllegalStateException("Address cannot be larger than three");
        }
        Address address = ToModel.toEntity(req);
        mongoTemplate.save(address);
        return ToModel.toRes(address);

    }

    @Override
    public List<AddressRes> getByUser() {
        List<AddressRes> addresses = mongoTemplate.find(new Query(Criteria.where("userId").is(userId)), Address.class)
                .stream()
                .map(ToModel::toRes).toList();
        return addresses;
    }

    @Override
    public void update(String addressId, AddressReq req) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(req, new TypeReference<Map<String, Object>>() {
        });

        Update update = new Update();
        map.forEach((key, value) -> {
            if (value != null) {
                update.set(key, value);
            }
        });

    }

    @Override
    public void delete(String addressId) {
        Address address = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), Address.class);

        if (address == null) {
            throw new RuntimeException();
        }
        mongoTemplate.remove(address);
    }

    @Override
    public AddressRes getAddressById(String addressId) {
        Address address = mongoTemplate.findOne(new Query(Criteria.where("id").is(addressId)), Address.class);

        if (address == null) {
            throw new RuntimeException();
        }
        return ToModel.toRes(address);
    }

}
