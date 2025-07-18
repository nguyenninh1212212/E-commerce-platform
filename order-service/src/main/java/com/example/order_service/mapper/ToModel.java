package com.example.order_service.mapper;

import com.example.order_service.model.Attributes;
import com.example.order_service.model.VariantPurchases;
import com.example.order_service.model.dto.req.OrderReq;
import com.example.order_service.model.dto.res.OrderRes;
import com.example.order_service.model.entity.Order;
import com.example.order_service.model.enums.PaymentMethod;
import com.example.order_service.model.enums.Status;
import com.example.order_service.util.AuthenticationUtil;

import lombok.experimental.UtilityClass;
import variant.VariantPurchase;

@UtilityClass
public class ToModel {
        public Order toEntity(OrderReq req) {

                return Order.builder()
                                .paid(req.getPayment_method() != PaymentMethod.CASH_ON_DELIVERY)
                                .userId(AuthenticationUtil.getUserId())
                                .address(req.getAddress())
                                .payment_method(req.getPayment_method())
                                .productPurchase(req.getProductPurchase())
                                .status(Status.PENDING)
                                .build();
        }

        public OrderRes toRes(Order order) {

                int totalQuantity = order.getQuantity();
                double totalPrice = order.getProductPurchase().getVariantPurchases().getPrice() * totalQuantity;
                return OrderRes.builder()
                                .id(order.getId())
                                .address(order.getAddress())
                                .payment_method(order.getPayment_method())
                                .productPurchase(order.getProductPurchase())
                                .total_quantity(totalQuantity)
                                .total_price(totalPrice)
                                .paid(order.isPaid())
                                .status(order.getStatus())
                                .build();
        }

        public Attributes toRes(shared.Attribute attribute) {
                return Attributes.builder()
                                .values(attribute.getValuesList())
                                .name(attribute.getName())
                                .build();
        }

        public VariantPurchases toRes(VariantPurchase variantPurchase) {
                return VariantPurchases.builder()
                                .price(variantPurchase.getPrice())
                                .variantId(variantPurchase.getId())
                                .attributes(variantPurchase.getAttributesList().stream().map(ToModel::toRes).toList())
                                .sku(variantPurchase.getSku())
                                .build();
        }

}
