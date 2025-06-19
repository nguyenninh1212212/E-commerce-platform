package com.example.product_service.service;

import java.util.List;

import com.example.product_service.model.dto.req.ProductReq;
import com.example.product_service.model.dto.res.Pagination;
import com.example.product_service.model.dto.res.ProductFeaturedRes;
import com.example.product_service.model.dto.res.ProductRes;

public interface ProductService {
    String addProduct(ProductReq req, List<String> img);

    ProductRes getProductById(String id);

    Pagination<List<ProductFeaturedRes>> getAllProducts(int page, int limit);

    Pagination<List<ProductFeaturedRes>> getProductsBySellerId(String sellerId);

    Pagination<List<ProductFeaturedRes>> getProductsByTag(String tag);

    void deleteProductById(String id);

}
