package com.example.variant_service.controller;

import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.VariantUserRes;
import com.example.variant_service.service.VariantServ;
import com.example.variant_service.util.AuthenticationUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/variants")
public class VariantController {

    @Autowired
    private VariantServ variantServ;

    @PostMapping("/batch")
    public void createVariants(
            @RequestBody @NotEmpty(message = "Danh sách variant không được để trống") List<@Valid VariantReq> variants,
            @RequestParam("productId") String productId) {

        variantServ.createVariantList(variants, productId);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<VariantUserRes>> getVariantsByProductId(
            @PathVariable String productId,
            @RequestParam(required = false) Map<String, Object> variantAttributes) {
        return ResponseEntity.ok(variantServ.findByProductId(productId));
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

    @DeleteMapping("/{productId}/{id}")
    public ResponseEntity<Void> deleteVariant(@PathVariable String productId, @PathVariable String id) {
        variantServ.deleteVariant(id, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteVariantByProduct(@PathVariable String productId) {
        variantServ.deleteVariantsByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}
