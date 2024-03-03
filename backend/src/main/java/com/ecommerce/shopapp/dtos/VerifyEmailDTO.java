package com.ecommerce.shopapp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyEmailDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    private String email;
}
