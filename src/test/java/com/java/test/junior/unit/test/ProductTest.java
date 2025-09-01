package com.java.test.junior.unit.test;

import com.java.test.junior.exception.ResourceConflictException;
import com.java.test.junior.exception.ResourceDeletedException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.service.ProductService.ProductService;
import com.java.test.junior.service.ProductService.ProductServiceImp;
import com.java.test.junior.service.ProductService.ProductServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Service
public class ProductTest {
    private final ProductMapper productMapper;
    private final ProductService productService;

    ProductTest() {
        productMapper = mock(ProductMapper.class);
        productService = new ProductServiceImp(productMapper);
    }

    @Test
    void checkGetProductById_valid() {
        Product product = new Product();
        product.setId(1L);
        product.setIsDeleted(false);

        when(productMapper.find(1L)).thenReturn(product);

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void checkGetProductById_absent() {
        when(productMapper.find(1L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void checkGetProductById_deleted() {
        Product product = new Product();
        product.setId(1L);
        product.setIsDeleted(true);

        when(productMapper.find(1L)).thenReturn(product);

        assertThrows(ResourceDeletedException.class, () -> productService.getProductById(1L));
    }
}
