package com.java.test.junior.service;

import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.Product;
import com.java.test.junior.model.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

    private final ProductMapper productMapper;

    @Override
    public ResponseEntity<Product> getProductById(Long id) {
        try {
            Product product = productMapper.findById(id);
            return (product != null)
                    ? ResponseEntity.ok(product)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Product>> getProductPage(Integer page, Integer size, String query) {
        try {
            List<Product> products = productMapper.getPage(
                    page,
                    size,
                    query);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> createProduct(ProductDTO product) {
        try {
            productMapper.insert(product);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> updateProduct(Long id, ProductDTO product) {
        try {
            int result = productMapper.update(id, product);
            return (result > 0)
                    ? ResponseEntity.ok().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long id) {
        try {
            int result = productMapper.delete(id);
            return (result > 0)
                    ? ResponseEntity.ok().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
