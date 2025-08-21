package com.java.test.junior.model.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDTO {
    @NotBlank
    private String name;
    @NotNull
    @Positive
    private Double price;
    @NotNull
    private String description;
}