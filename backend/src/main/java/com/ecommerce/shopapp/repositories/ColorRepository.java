package com.ecommerce.shopapp.repositories;

import com.ecommerce.shopapp.models.Color;
import com.ecommerce.shopapp.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findAllByIdIn(Iterable<Long> colorIds);
}
