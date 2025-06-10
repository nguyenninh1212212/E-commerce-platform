package com.example.product_coordinator_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.product_coordinator_service.model.dto.req.Product;
import com.example.product_coordinator_service.model.dto.req.Variant;
import com.example.product_coordinator_service.service.ProductCoordinatorService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/product-variant")
@RequiredArgsConstructor
public class ProductCoordinatorController {
    private final ProductCoordinatorService coordinatorService;

    @PostMapping("/cre")
    public ResponseEntity<?> createProductAndVariant(
            @RequestPart Product product,
            @RequestPart List<Variant> variant,
            @RequestPart MultipartFile file

    ) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = coordinatorService.CreateProductAndVariants(product, variant, file);
        return ResponseEntity.ok(future.get().toString());
    }

}
