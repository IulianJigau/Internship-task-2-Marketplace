package com.java.test.junior.service.ProductService;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<Product> getProductById(Long productId);

    ResponseEntity<List<Product>> getProductPage(Integer page, Integer size, String query);

    ResponseEntity<String> createProduct(ProductDTO product,  ExtendedUserDetails userDetails);

    ResponseEntity<String> updateProduct(Long productId, ProductDTO product,  ExtendedUserDetails userDetails);

    ResponseEntity<String> deleteProduct(Long productId,  ExtendedUserDetails userDetails);
}
