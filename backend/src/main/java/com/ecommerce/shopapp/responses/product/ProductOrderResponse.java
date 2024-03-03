package com.ecommerce.shopapp.responses.product;

import com.ecommerce.shopapp.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ProductOrderResponse {
    private Long id;
    private String name;
    private String image;
    private Float price;
    private Integer quantity;

    public static ProductOrderResponse fromProductOrder(Product product) {
        String firstImageUrl = null;
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            firstImageUrl = product.getImages().get(0).getUrl();
        }

        return ProductOrderResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .image(firstImageUrl)
                .price(product.getPrice())
                .build();
    };
}
