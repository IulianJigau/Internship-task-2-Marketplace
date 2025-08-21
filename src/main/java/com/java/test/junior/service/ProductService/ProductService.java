package com.java.test.junior.service.ProductService;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<?> getProductById(Long productId);

    ResponseEntity<?> getProductPage(Integer page, Integer size, String query);

    ResponseEntity<?> createProduct(ProductDTO product,  ExtendedUserDetails userDetails);

    ResponseEntity<?> updateProduct(Long productId, ProductDTO product,  ExtendedUserDetails userDetails);

    ResponseEntity<?> deleteProduct(Long productId,  ExtendedUserDetails userDetails);
}
