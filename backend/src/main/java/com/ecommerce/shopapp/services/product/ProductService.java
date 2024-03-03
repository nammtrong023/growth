package com.ecommerce.shopapp.services.product;

import com.ecommerce.shopapp.dtos.ProductDTO;
import com.ecommerce.shopapp.models.Product;
import com.ecommerce.shopapp.responses.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDTO productDTO);

    Product getProductById(Long productId) throws Exception;

    Page<ProductResponse> getAllProducts(String search, PageRequest pageRequest);

    Product updateProduct(Long productId, ProductDTO productDTO);

    List<Product> getFeaturedProducts();

    List<Product> getSimilarProducts(Long productId);

    Page<ProductResponse> filterProducts(Long categoryId,
                                         Long sizeId,
                                         Long colorId,
                                         Double price,
                                         String sortPrice,
                                         String sortCreatedAt,
                                         PageRequest pageRequest);

    void deleteProduct(Long productId);
}
