package com.example.variant_service.annotation;

import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@checkIsOwner.IsSeller(#variantId)")
public @interface IsSeller {

}
