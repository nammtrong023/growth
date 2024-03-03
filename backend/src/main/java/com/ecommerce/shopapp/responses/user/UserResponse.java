package com.ecommerce.shopapp.responses.user;

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
public class UserResponse {
    private Long id;

    @JsonProperty("full_name")
    private String fullName;
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;


    public static UserResponse userResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }
}
