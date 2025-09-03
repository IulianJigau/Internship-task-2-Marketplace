package com.java.test.junior.service.product;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.model.RequestResponses.PaginationResponse;

public interface ProductService {

    Product getProductById(Long productId);

    PaginationResponse<?> getProductsPage(Integer page, Integer size, Boolean refresh, String query, Long userId, Boolean isDeleted);

    Product createProduct(ProductDTO product, ExtendedUserDetails userDetails);

    void updateProduct(Long productId, ProductDTO product, ExtendedUserDetails userDetails);

    void deleteProduct(Long productId, ExtendedUserDetails userDetails);

    void clearDeletedProducts();
}
