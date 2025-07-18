package com.example.product_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.product_service.annotation.SellerOnly;
import com.example.product_service.model.dto.req.product.ProductReq;
import com.example.product_service.model.dto.req.product.ProductUpdateReq;
import com.example.product_service.model.dto.req.variant.VariantReq;
import com.example.product_service.model.dto.res.ApiRes;
import com.example.product_service.model.dto.res.Pagination;
import com.example.product_service.model.dto.res.ProductFeaturedRes;
import com.example.product_service.model.dto.res.ProductRes;
import com.example.product_service.service.ProductService;
import com.example.product_service.validator.create;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

        private final ProductService productService;

        @PostMapping
        public ResponseEntity<ApiRes<String>> createProduct(@RequestBody @Validated(create.class) ProductReq product) {
                log.info(product.getName());
                return ResponseEntity.ok(
                                ApiRes.<String>builder()
                                                .status(HttpStatus.OK.value())
                                                .data(productService.addProduct(product))
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
        public ResponseEntity<ApiRes<Pagination<List<ProductFeaturedRes>>>> getAllProducts(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int limit) {
                return ResponseEntity.ok(
                                ApiRes.<Pagination<List<ProductFeaturedRes>>>builder()
                                                .status(HttpStatus.OK.value())
                                                .data(productService.getAllProducts(page, limit))
                                                .build());
        }

        @GetMapping("/seller/{sellerId}")
        public ResponseEntity<ApiRes<Pagination<List<ProductFeaturedRes>>>> getProductsBySeller(
                        @PathVariable String sellerId) {
                return ResponseEntity.ok(
                                ApiRes.<Pagination<List<ProductFeaturedRes>>>builder()
                                                .status(HttpStatus.OK.value())
                                                .data(productService.getProductsBySellerId(sellerId))
                                                .build());

        }

        @GetMapping("/tag/{tag}")
        public ResponseEntity<ApiRes<Pagination<List<ProductFeaturedRes>>>> getProductsByTag(@PathVariable String tag) {
                return ResponseEntity.ok(
                                ApiRes.<Pagination<List<ProductFeaturedRes>>>builder()
                                                .status(HttpStatus.OK.value())
                                                .data(productService.getProductsByTag(tag))
                                                .build());

        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiRes<String>> deleteProduct(@PathVariable String id) {
                productService.deleteProductById(id);
                return ResponseEntity.ok(
                                ApiRes.<String>builder()
                                                .status(HttpStatus.NO_CONTENT.value())
                                                .data("Product deleted successfully")
                                                .build());

        }

        @PatchMapping("/{id}")
        public ResponseEntity<Void> updateProduct(
                        @PathVariable String id,
                        @RequestBody ProductUpdateReq updates) {
                productService.updateProduct(id, updates);
                return ResponseEntity.noContent().build();
        }
}
