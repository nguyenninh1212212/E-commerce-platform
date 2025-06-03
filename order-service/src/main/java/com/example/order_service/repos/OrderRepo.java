package com.example.order_service.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.order_service.model.entity.Order;

@Repository
public interface OrderRepo extends MongoRepository<Order, String> {

}
