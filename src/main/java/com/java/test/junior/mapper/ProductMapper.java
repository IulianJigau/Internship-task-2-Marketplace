package com.java.test.junior.mapper;

import com.java.test.junior.model.Product;
import com.java.test.junior.model.ProductDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    Product findById(Long id);

    List<Product> getPage(@Param("page") Integer page, @Param("size") Integer size);

    void insert(ProductDTO product);

    int update(@Param("id") Long id, @Param("product") ProductDTO product);

    int delete(Long id);

}
