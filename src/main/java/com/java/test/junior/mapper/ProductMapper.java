package com.java.test.junior.mapper;

import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;

import java.util.List;

public interface ProductMapper {

    boolean exists(Long id);

    Product find(Long id);

    List<Product> getPage(Integer page, Integer size, String query);

    void insert(Long userId, ProductDTO product);

    void update(Long id, ProductDTO product);

    void delete(Long id);
}
