package com.ecommerce.shopapp.repositories;

import com.ecommerce.shopapp.models.Order;
import com.ecommerce.shopapp.responses.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails WHERE o.user.id = :userId")
    List<Order> findByUserId(Long userId);
}
