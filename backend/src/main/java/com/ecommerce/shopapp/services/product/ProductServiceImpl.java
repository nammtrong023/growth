package com.ecommerce.shopapp.services.product;

import com.ecommerce.shopapp.dtos.ImageDTO;
import com.ecommerce.shopapp.dtos.ProductDTO;
import com.ecommerce.shopapp.exceptions.DataNotFoundException;
import com.ecommerce.shopapp.models.*;
import com.ecommerce.shopapp.repositories.ImageRepository;
import com.ecommerce.shopapp.repositories.ProductRepository;
import com.ecommerce.shopapp.responses.product.ProductResponse;
import com.ecommerce.shopapp.services.category.CategoryService;
import com.ecommerce.shopapp.services.color.ColorService;
import com.ecommerce.shopapp.services.size.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;
    private final CategoryService categoryService;
    private final ImageRepository imageRepo;
    private final SizeService sizeService;
    private final ColorService colorService;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Category existingCategory = categoryService.existingCategory(productDTO.getCategoryId());
        List<Size> existingSizes = sizeService.existingSizes(productDTO.getSizeIds());
        List<Color> existingColors = colorService.existingColors(productDTO.getColorIds());

        Product newProduct = Product.builder()
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .description(productDTO.getDescription())
                    .isFeatured(productDTO.isFeatured())
                    .category(existingCategory)
                    .sizes(existingSizes)
                    .colors(existingColors)
                    .build();

        List<Image> images = this.bulkCreateImages(productDTO.getImages(), newProduct);
        newProduct.setImages(images);

        return productRepo.save(newProduct);
    }

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepo.getDetailProduct(productId);
        if(optionalProduct.isPresent()) {
            return optionalProduct.get();
        }

        throw new DataNotFoundException("Cannot find product with id: " + productId);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, PageRequest pageRequest) {
        Page<Product> productsPage;
        productsPage = productRepo.searchProducts(keyword, pageRequest);
        return productsPage.map(ProductResponse::productResponse);
    }

    @Override
    public List<Product> getFeaturedProducts() {
        return productRepo.findByIsFeaturedTrue();
    }

    @Override
    public Page<ProductResponse> filterProducts(Long categoryId,
                                                Long sizeId,
                                                Long colorId,
                                                Double price,
                                                String sortPrice,
                                                String sortCreatedAt,
                                                PageRequest pageRequest) {
        Page<Product> productsPage;
        productsPage = productRepo.findByFilter(
                categoryId, sizeId, colorId, price, sortPrice, sortCreatedAt, pageRequest);

        return productsPage.map(ProductResponse::productResponse);
    }

    @Override
    public List<Product> getSimilarProducts(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        return productRepo.findByCategoryAndIdNot(product.getCategory(), productId);
    }

    @Override
    @Transactional
    public Product updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));

        Category existingCategory = categoryService.existingCategory(productDTO.getCategoryId());
        List<Size> existingSizes = sizeService.existingSizes(productDTO.getSizeIds());
        List<Color> existingColors = colorService.existingColors(productDTO.getColorIds());

        product.getSizes().clear();
        product.getColors().clear();

        List<Image> existingImages = product.getImages();
        for (Image existingImage : existingImages) {
            imageRepo.delete(existingImage);
        }
        existingImages.clear();

        List<Image> images = this.bulkCreateImages(productDTO.getImages(), product);

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImages(images);
        product.setFeatured(productDTO.isFeatured());
        product.setCategory(existingCategory);
        product.setColors(existingColors);
        product.setSizes(existingSizes);

        return productRepo.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepo.findById(id);

        if (optionalProduct.isEmpty()) {
            throw new DataNotFoundException("Product not found with id: " + id);
        }

        productRepo.deleteById(id);
    }

    @Transactional
    public List<Image> bulkCreateImages(List<ImageDTO> imageDTOs, Product product) {
        List<Image> images = new ArrayList<>();

        for (ImageDTO imageDTO : imageDTOs) {
            Image image = Image.builder()
                    .url(imageDTO.getUrl())
                    .product(product)
                    .build();

            images.add(image);
        }

        return imageRepo.saveAll(images);
    }
}
