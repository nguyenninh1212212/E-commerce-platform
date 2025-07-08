package com.example.variant_service.service;

import java.util.List;

import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.req.VariantUpdateReq;
import com.example.variant_service.model.dto.res.VariantUserRes;

public interface VariantServ {

    List<VariantUserRes> findByProductId(String productId);

    void createVariantList(List<VariantReq> variant, String productId);

    void deleteVariant(String id, String productId);

    void updateVariantPartial(VariantReq req, String id, String productId);

    void updateVariantList(List<VariantUpdateReq> req, String productId);

    void deleteVariantsByProductId(String productId);
}
