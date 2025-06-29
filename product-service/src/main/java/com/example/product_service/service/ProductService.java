package com.example.product_service.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.product_service.model.dto.req.ProductReq;
import com.example.product_service.model.dto.req.VariantReq;
import com.example.product_service.model.dto.res.Pagination;
import com.example.product_service.model.dto.res.ProductFeaturedRes;
import com.example.product_service.model.dto.res.ProductRes;

public interface ProductService {

    void addProduct(ProductReq req, List<MultipartFile> imgs, List<VariantReq> variants);

    ProductRes getProductById(String id);

    Pagination<List<ProductFeaturedRes>> getAllProducts(int page, int limit);

    Pagination<List<ProductFeaturedRes>> getProductsBySellerId(String sellerId);

    Pagination<List<ProductFeaturedRes>> getProductsByTag(String tag);

    void deleteProductById(String id);

}
