package com.ecommerce.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private Float price;

    @NotBlank(message = "Description is required")
    private String description;

    @JsonProperty("is_featured")
    private boolean isFeatured;

    @JsonProperty("category_id")
    @NotNull(message = "Category Id is required")
    private Long categoryId;

    @Valid
    @NotNull(message = "Image url must not be null")
    private List<ImageDTO> images;

    @NotEmpty(message = "Size Ids list is required")
    @JsonProperty("size_ids")
    private List<Long> sizeIds;

    @NotEmpty(message = "Color Ids list is required")
    @JsonProperty("color_ids")
    private List<Long> colorIds;
}
