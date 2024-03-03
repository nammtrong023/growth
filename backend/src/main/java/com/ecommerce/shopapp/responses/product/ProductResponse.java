package com.ecommerce.shopapp.responses.product;

import com.ecommerce.shopapp.models.*;
import com.ecommerce.shopapp.responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private String name;
    private Float price;
    private String description;

    @JsonProperty("is_featured")
    private boolean isFeatured;

    private List<Image> images;
    private List<Size> sizes;
    private List<Color> colors;

    @JsonProperty("category_id")
    private Long categoryId;

    public static ProductResponse productResponse(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .isFeatured(product.isFeatured())
                .images(product.getImages())
                .sizes(product.getSizes())
                .colors(product.getColors())
                .categoryId(product.getCategory().getId())
                .build();

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;
    }
}
