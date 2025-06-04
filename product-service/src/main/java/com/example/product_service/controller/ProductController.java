package com.example.product_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.product_service.model.dto.req.ProductReq;
import com.example.product_service.model.dto.res.ApiRes;
import com.example.product_service.model.dto.res.ProductFeaturedRes;
import com.example.product_service.model.dto.res.ProductRes;
import com.example.product_service.service.ProductServ;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

        private final ProductServ productService;

        @PostMapping
        public ResponseEntity<ApiRes<String>> addProduct(
                        @RequestPart("req") ProductReq req,
                        @RequestPart("img") MultipartFile[] img) {
                productService.addProduct(req, img[0]);
                return ResponseEntity.ok(
                                ApiRes.<String>builder()
                                                .status(HttpStatus.CREATED.value())
                                                .data("Product added successfully")
                                                .build());
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiRes<ProductRes>> getProductById(@PathVariable String id) {
                ProductRes productRes = productService.getProductById(id);
                return ResponseEntity.ok(
                                ApiRes.<ProductRes>builder()
                                                .status(HttpStatus.OK.value())
                                                .data(productRes)
                                                .build());
        }

        @GetMapping
        public ResponseEntity<ApiRes<List<ProductFeaturedRes>>> getAllProducts() {
                return ResponseEntity.ok(
                                ApiRes.<List<ProductFeaturedRes>>builder()
                                                .status(HttpStatus.OK.value())
                                                .data(productService.getAllProducts())
                                                .build());
        }
}
