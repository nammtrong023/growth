package com.ecommerce.shopapp.responses.order;

import com.ecommerce.shopapp.models.Order;
import com.ecommerce.shopapp.models.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class OrderResponse {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("total_price")
    private Float totalPrice;

    private String address;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetails;

    public static OrderResponse fromOrder(Order order) {
        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .collect(Collectors.toList());

        return OrderResponse
                .builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .address(order.getAddress())
                .phoneNumber(order.getPhoneNumber())
                .orderDate(order.getOrderDate())
                .orderDetails(orderDetailResponses)
                .build();
    }
}
