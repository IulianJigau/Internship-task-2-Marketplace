package com.java.test.junior.mapper;

import com.java.test.junior.model.Product;
import com.java.test.junior.model.ProductDTO;

import java.util.List;

public interface ProductMapper {
    Product findById(Long id);

    List<Product> getPage(Integer page, Integer size, String query);

    void insert(ProductDTO product);

    int update(Long id, ProductDTO product);

    int delete(Long id);
}
