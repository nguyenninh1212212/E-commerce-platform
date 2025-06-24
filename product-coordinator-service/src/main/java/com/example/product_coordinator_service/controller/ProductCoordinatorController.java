package com.example.product_coordinator_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.product_coordinator_service.model.dto.req.Product;
import com.example.product_coordinator_service.model.dto.req.Variant;
import com.example.product_coordinator_service.model.dto.res.ApiRes;
import com.example.product_coordinator_service.service.ProductCoordinatorService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
public class ProductCoordinatorController {
        private final ProductCoordinatorService coordinatorService;

        @PostMapping("/cre")
        public ResponseEntity<ApiRes<String>> createProductAndVariant(
                        @RequestPart Product product,
                        @RequestPart List<Variant> variant,
                        @RequestPart MultipartFile[] file

        ) {
                coordinatorService.CreateProductAndVariants(product, variant, file);
                ApiRes<String> response = ApiRes.<String>builder()
                                .data("Product create successfully!!")
                                .build();
                return ResponseEntity.ok(response);
        }

        @PostMapping("/del")
        public ResponseEntity<ApiRes<String>> deleteProductAndVariant(
                        @RequestParam String product

        ) {
                String result = coordinatorService.DeleteProductAndVariantsById(product);
                ApiRes<String> response = ApiRes.<String>builder()
                                .data(result)
                                .build();
                return ResponseEntity.ok(response);
        }

}
