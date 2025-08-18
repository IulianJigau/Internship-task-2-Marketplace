package com.java.test.junior.service;

import com.java.test.junior.model.Product;
import com.java.test.junior.model.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<Product> getProductById(Long id);

    ResponseEntity<List<Product>> getProductPage(Integer page, Integer size);

    ResponseEntity<String> createProduct(ProductDTO product);

    ResponseEntity<String> updateProduct(Long id, ProductDTO product);

    ResponseEntity<String> deleteProduct(Long id);
}
