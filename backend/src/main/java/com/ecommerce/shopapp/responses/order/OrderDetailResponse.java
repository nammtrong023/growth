package com.ecommerce.shopapp.responses.order;

import com.ecommerce.shopapp.models.OrderDetail;
import com.ecommerce.shopapp.responses.product.ProductOrderResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private String size;
    private String color;
    private Float price;

    @JsonProperty("product")
    private ProductOrderResponse productOrderResponse;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        ProductOrderResponse productOrderResponse =
                ProductOrderResponse.fromProductOrder(orderDetail.getProduct());

        return OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .size(orderDetail.getSize())
                .color(orderDetail.getColor())
                .price(orderDetail.getPrice())
                .productOrderResponse(productOrderResponse)
                .build();
    };
}
