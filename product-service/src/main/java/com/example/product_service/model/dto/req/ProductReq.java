package com.example.product_service.model.dto.req;

import org.springframework.web.multipart.MultipartFile;

import com.example.product_service.model.dto.ProductBase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProductReq extends ProductBase {
}
