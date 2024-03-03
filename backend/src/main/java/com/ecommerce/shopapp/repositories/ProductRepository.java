package com.ecommerce.shopapp.repositories;

import com.ecommerce.shopapp.models.Category;
import com.ecommerce.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByIsFeaturedTrue();
    List<Product> findByCategoryAndIdNot(Category category, Long productId);

    @Query("SELECT DISTINCT p FROM Product p " +
            "WHERE (:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:sizeId IS NULL OR :sizeId = 0 OR :sizeId IN (SELECT s.id FROM p.sizes s)) " +
            "AND (:colorId IS NULL OR :colorId = 0 OR :colorId IN (SELECT c.id FROM p.colors c)) " +
            "AND (:price IS NULL OR :price = 0 OR p.price <= :price)" +
            "ORDER BY " +
            "CASE WHEN :sortCreatedAt = 'desc' THEN p.createdAt END DESC, " +
            "CASE WHEN :sortPrice = 'desc' THEN p.price END DESC, " +
            "CASE WHEN :sortPrice = 'asc' THEN p.price END ASC")
    Page<Product> findByFilter(
            @Param("categoryId") Long categoryId,
            @Param("sizeId") Long sizeId,
            @Param("colorId") Long colorId,
            @Param("price") Double price,
            @Param("sortPrice") String sortPrice,
            @Param("sortCreatedAt") String sortCreatedAt,
            Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.images " +
            "WHERE (:search IS NULL OR :search = '' OR p.name LIKE %:search%" +
            " OR p.category.name LIKE %:search%)")
    Page<Product> searchProducts(
            @Param("search") String search,
            Pageable pageable);


    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :productId")
    Optional<Product> getDetailProduct(@Param("productId") Long productId);
}
