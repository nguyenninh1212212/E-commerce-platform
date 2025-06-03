package com.example.product_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.model.dto.req.ProductReq;
import com.example.product_service.model.dto.res.ProductRes;
import com.example.product_service.service.ProductServ;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServ productService; 

    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody ProductReq req) {
        productService.addProduct(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRes> getProductById(@PathVariable String id) {
        ProductRes productRes = productService.getProductById(id);
        return ResponseEntity.ok(productRes);
    }
}
