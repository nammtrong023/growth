package com.ecommerce.shopapp.services.user;

import com.ecommerce.shopapp.dtos.UpdateUserDTO;
import com.ecommerce.shopapp.responses.user.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    UserResponse getCurrentUser(HttpServletRequest request);
    UserResponse updateUser(HttpServletRequest request, UpdateUserDTO updateUserDTO,Long id);
}
