package com.ecommerce.shopapp.services.user;

import com.ecommerce.shopapp.config.JwtService;
import com.ecommerce.shopapp.dtos.UpdateUserDTO;
import com.ecommerce.shopapp.exceptions.UnauthorizedException;
import com.ecommerce.shopapp.models.TokenType;
import com.ecommerce.shopapp.models.User;
import com.ecommerce.shopapp.repositories.UserRepository;
import com.ecommerce.shopapp.responses.user.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final JwtService jwtService;

    @Override
    public UserResponse getCurrentUser(HttpServletRequest request) {
        User user = jwtService.getTokenFromHeader(request, TokenType.ACCESS_TYPE);

        return UserResponse.userResponse(user);
    }

    @Override
    public UserResponse updateUser(HttpServletRequest request, UpdateUserDTO updateUserDTO, Long id) {
        User user = jwtService.getTokenFromHeader(request, TokenType.ACCESS_TYPE);

        if(!user.getId().equals(id)) {
            throw new UnauthorizedException();
        }

        user.setFullName(updateUserDTO.getFullName());
        user.setPhoneNumber(updateUserDTO.getPhoneNumber());
        user.setAddress(updateUserDTO.getAddress());
        userRepo.save(user);

        return UserResponse.userResponse(user);
    }
}
