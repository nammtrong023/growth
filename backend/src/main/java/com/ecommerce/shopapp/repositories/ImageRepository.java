package com.ecommerce.shopapp.repositories;

import com.ecommerce.shopapp.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
