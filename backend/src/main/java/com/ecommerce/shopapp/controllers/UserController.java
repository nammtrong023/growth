package com.ecommerce.shopapp.controllers;


import com.ecommerce.shopapp.dtos.UpdateUserDTO;
import com.ecommerce.shopapp.responses.user.UserResponse;
import com.ecommerce.shopapp.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;

    @GetMapping("current-user")
    public ResponseEntity<UserResponse> getCurrentUser(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getCurrentUser(request));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(
            @Valid @RequestBody UpdateUserDTO updateUserDTO,
            HttpServletRequest request,
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.updateUser(request, updateUserDTO, id));
    }
}
