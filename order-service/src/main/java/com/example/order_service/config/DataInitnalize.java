package com.example.order_service.config;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.example.order_service.excep.AlreadyExist;
import com.example.order_service.model.entity.PaymentMethod;
import com.example.order_service.model.enums.PAYMENTMETHOD;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitnalize implements CommandLineRunner {
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        Stream.of(PAYMENTMETHOD.CASH_ON_DELIVERY, PAYMENTMETHOD.CREDIT_CARD, PAYMENTMETHOD.DEBIT_CARD,
                PAYMENTMETHOD.E_WALLET, PAYMENTMETHOD.PAYPAL).forEach(this::typePayment);
    }

    private void typePayment(PAYMENTMETHOD type) {
        PaymentMethod method = mongoTemplate.findOne(new Query(Criteria.where("type").is(type)), PaymentMethod.class);
        if (method != null) {
            throw new AlreadyExist("Payment method was already exist");
        } else {
            PaymentMethod newPayment = PaymentMethod.builder()
                    .type(type)
                    .useable(true)
                    .build();
            mongoTemplate.save(newPayment);
        }

    }

}
