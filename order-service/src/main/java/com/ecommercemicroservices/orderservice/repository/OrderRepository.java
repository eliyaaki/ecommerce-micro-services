package com.ecommercemicroservices.orderservice.repository;

import com.ecommercemicroservices.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
