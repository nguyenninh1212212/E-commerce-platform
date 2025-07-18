package com.example.order_service.annotation;

import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@checkIsOwner.IsSeller(#orderId) and hasRole('SELLER')")
public @interface IsSeller {

}
