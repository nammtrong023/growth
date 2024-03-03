package com.ecommerce.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColorDTO {
    @NotBlank(message = "Value is required")
    private String value;

    @NotBlank(message = "Name is required")
    private String name;
}
