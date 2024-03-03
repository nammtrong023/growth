package com.ecommerce.shopapp.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.shopapp.models.*;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

