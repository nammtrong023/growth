package com.ecommerce.shopapp.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterDTO {

    private Long categoryId;
    private Long sizeId;
    private Long colorId;
    private Double price;
    private String sortPrice;
}
