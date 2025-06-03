package com.example.variant_service.controller;

import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.ApiRes;
import com.example.variant_service.model.dto.res.VariantRes;
import com.example.variant_service.service.VariantServ;
import com.example.variant_service.validation.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/variants")
public class VariantController {

    @Autowired
    private VariantServ variantServ;

    // GET /variants/product/{productId}?attr_key1=value1&attr_key2=value2
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<VariantRes>> getVariantsByProductId(
            @PathVariable String productId,
            @RequestParam(required = false) Map<String, Object> variantAttributes) {
        List<VariantRes> variants;
        if (variantAttributes == null || variantAttributes.isEmpty()) {
            variants = variantServ.findByProductId(productId);
        } else {
            variants = variantServ.findByProductIdWithAttributes(productId, variantAttributes);
        }
        return ResponseEntity.ok(variants);
    }

    // POST /api/variants
    @PostMapping("/product/{productId}")
    public ResponseEntity<ApiRes> createVariant(@PathVariable String productId,
            @RequestBody @Validated(add.class) VariantReq variantReq) {
        variantServ.createVariant(variantReq, productId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiRes.builder()
                        .message("Variant created successfully")
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    // PATCH /api/variants/{id}
    @PatchMapping("/{productId}/{id}")
    public ResponseEntity<Void> updateVariantPartial(
            @PathVariable String productId,
            @PathVariable String id,
            @RequestBody VariantReq variantReq) {
        variantServ.updateVariantPartial(variantReq, id, productId);
        return ResponseEntity.ok().build();

    }

    // DELETE /api/variants/{id}
    @DeleteMapping("/{productId}/{id}")
    public ResponseEntity<Void> deleteVariant(@PathVariable String productId, @PathVariable String id) {
        variantServ.deleteVariant(id, productId);
        return ResponseEntity.noContent().build();
    }
}
