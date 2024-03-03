package com.ecommerce.shopapp.repositories;

import com.ecommerce.shopapp.models.Size;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface SizeRepository extends JpaRepository<Size, Long> {
    List<Size> findAllByIdIn(Iterable<Long> sizeIds);
}
