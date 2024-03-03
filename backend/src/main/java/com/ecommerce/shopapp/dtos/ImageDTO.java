package com.ecommerce.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageDTO {
    @NotBlank(message = "URL is required")
    private String url;
}
