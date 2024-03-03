package com.ecommerce.shopapp.services.order;


import com.ecommerce.shopapp.dtos.OrderDetailDTO;
import com.ecommerce.shopapp.dtos.OrderWithDetailsDTO;
import com.ecommerce.shopapp.exceptions.DataNotFoundException;
import com.ecommerce.shopapp.models.*;
import com.ecommerce.shopapp.repositories.OrderDetailRepository;
import com.ecommerce.shopapp.repositories.OrderRepository;
import com.ecommerce.shopapp.repositories.ProductRepository;
import com.ecommerce.shopapp.repositories.UserRepository;
import com.ecommerce.shopapp.responses.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public Order createOrder(OrderWithDetailsDTO orderWithDetailsDTO) {
        User user = userRepository.findById(orderWithDetailsDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user"));

        Order order = Order.builder()
                .user(user)
                .fullName(orderWithDetailsDTO.getFullName())
                .email(orderWithDetailsDTO.getEmail())
                .phoneNumber(orderWithDetailsDTO.getPhoneNumber())
                .address(orderWithDetailsDTO.getAddress())
                .totalPrice(orderWithDetailsDTO.getTotalPrice())
                .paymentMethod(orderWithDetailsDTO.getPaymentMethod())
                .orderDate(LocalDate.now())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailDTO orderDetailDTO : orderWithDetailsDTO.getOrderDetailDTOS()) {
            Product product = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Product not found"));

            OrderDetail orderDetail = OrderDetail
                    .builder()
                    .order(order)
                    .product(product)
                    .quantity(orderDetailDTO.getQuantity())
                    .price(orderDetailDTO.getPrice())
                    .size(orderDetailDTO.getSize())
                    .color(orderDetailDTO.getColor())
                    .build();

            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }
}
