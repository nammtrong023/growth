package com.ecommerce.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    @JsonProperty("name")
    @NotEmpty(message = "Category's name cannot be empty")
    private String name;

    @JsonProperty("image")
    @NotEmpty(message = "Image cannot be empty")
    private String image;
}
