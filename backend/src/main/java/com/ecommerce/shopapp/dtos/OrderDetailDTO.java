package com.ecommerce.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {
    @Min(value = 0, message = "Product's ID must be > 0")
    @JsonProperty("product_id")
    private Long productId;

    @Min(value=0, message = "Price must be >= 0")
    private Float price;

    @Min(value=0, message = "Quantity must be >= 0")
    private Integer quantity;

    @NotBlank(message = "Color is required")
    private String color;

    @NotBlank(message = "Size is required")
    private String size;
}
