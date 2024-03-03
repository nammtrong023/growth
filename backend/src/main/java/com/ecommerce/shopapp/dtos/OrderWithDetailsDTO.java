package com.ecommerce.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderWithDetailsDTO {
    @JsonProperty("user_id")
    @Min(value = 0, message = "User's ID must be > 0")
    private Long userId;

    @JsonProperty("full_name")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 11, message = "Password must be at least 10 characters long")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @JsonProperty("total_price")
    @Min(value = 0, message = "Total price must be >= 0")
    private Float totalPrice;

    @NotBlank(message = "Payment method is required")
    @JsonProperty("payment_method")
    private String paymentMethod;

    @Valid
    @JsonProperty("order_details")
    private List<OrderDetailDTO> orderDetailDTOS;
}