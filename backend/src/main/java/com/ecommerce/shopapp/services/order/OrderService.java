package com.ecommerce.shopapp.services.order;

import com.ecommerce.shopapp.dtos.OrderWithDetailsDTO;
import com.ecommerce.shopapp.models.Order;
import com.ecommerce.shopapp.responses.order.OrderResponse;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderWithDetailsDTO orderWithDetailsDTO) throws Exception;
    Order getOrderById(Long id);
    List<OrderResponse> findByUserId(Long userId);
}
