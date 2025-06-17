package com.example.variant_service.service;

import java.util.List;
import java.util.Map;

import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.VariantRes;

public interface VariantServ {

    List<VariantRes> findByProductIdWithAttributes(String productId, Map<String, Object> attrs);

    List<VariantRes> findByProductId(String productId);

    void createVariant(VariantReq variant, String productId);

    void createVariantList(List<VariantReq> variant, String productId);

    void deleteVariant(String id, String productId);

    void updateVariantPartial(VariantReq req, String id, String productId);

    void deleteVariantsByProductId(String productId);
}
