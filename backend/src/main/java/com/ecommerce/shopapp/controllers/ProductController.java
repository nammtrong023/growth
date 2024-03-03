package com.ecommerce.shopapp.controllers;

import com.ecommerce.shopapp.dtos.ProductDTO;
import com.ecommerce.shopapp.models.Product;
import com.ecommerce.shopapp.responses.product.ProductListResponse;
import com.ecommerce.shopapp.responses.product.ProductResponse;
import com.ecommerce.shopapp.services.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product savedProduct = productService.createProduct(productDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("featured-products")
    public ResponseEntity<List<Product>> getFeaturedProducts() {
        List<Product> products = productService.getFeaturedProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit);

        Page<ProductResponse> productPage = productService.getAllProducts(search, pageRequest);

        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(products)
                .currentPage(page)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("filter")
    public ResponseEntity<ProductListResponse> filterProducts(
            @RequestParam(value = "category_id", required = false) Long categoryId,
            @RequestParam(value = "size_id", required = false) Long sizeId,
            @RequestParam(value = "color_id", required = false) Long colorId,
            @RequestParam(value = "sort_price", required = false) String sortPrice,
            @RequestParam(value = "sort_created_at", required = false) String sortCreatedAt,
            @RequestParam(required = false) Double price,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        Page<ProductResponse> productPage = productService.filterProducts(
                categoryId, sizeId, colorId, price, sortPrice, sortCreatedAt, pageRequest);

        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(products)
                .currentPage(page)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("similar/{product_id}")
    public ResponseEntity<List<Product>> getSimilarProducts(
            @PathVariable("product_id") Long productId) {
        List<Product> products = productService.getSimilarProducts(productId);

        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) throws Exception {
        Product product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(
            @Valid
            @PathVariable("id") Long productId,
            @RequestBody ProductDTO productDTO
    ) {
        Product updatedProduct = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", productId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}