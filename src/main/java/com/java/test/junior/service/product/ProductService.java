package com.java.test.junior.service.product;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.product.Product;
import com.java.test.junior.model.product.ProductDTO;
import com.java.test.junior.model.response.PaginationResponse;

public interface ProductService {

    Boolean existsProductId(Long id);

    Product getProductById(Long productId);

    PaginationResponse<?> getProductsPage(PaginationOptionsDTO paginationOptions, String query, Long userId, Boolean isDeleted);

    Product createProduct(ProductDTO product, ExtendedUserDetails userDetails);

    void updateProduct(Long productId, ProductDTO product, ExtendedUserDetails userDetails);

    void deleteProduct(Long productId, ExtendedUserDetails userDetails);

    void clearDeletedProducts();

    void copyStagingProducts(Long userId);

    Product scrollProducts(String query, Boolean refresh, ExtendedUserDetails userDetails);
}