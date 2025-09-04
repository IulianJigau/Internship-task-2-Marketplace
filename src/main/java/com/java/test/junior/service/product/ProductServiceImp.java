package com.java.test.junior.service.product;

import com.java.test.junior.component.RoleChecker;
import com.java.test.junior.exception.ResourceDeletedException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.model.RequestResponse.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductMapper productMapper;
    private final RoleChecker roleChecker;

    @Override
    public Product getProductById(Long productId) {
        Product product = productMapper.find(productId);
        if (product == null) {
            throw new ResourceNotFoundException("The requested product was not found.");
        }
        if (product.getIsDeleted()) {
            throw new ResourceDeletedException("The requested product was deleted.");
        }
        return product;
    }

    @Override
    public PaginationResponse<?> getProductsPage(PaginationOptionsDTO paginationOptions, String query, Long userId, Boolean isDeleted) {
        List<Product> products = productMapper.getPage(paginationOptions.getPage(), paginationOptions.getPageSize(), query, userId, isDeleted);
        long entries = -1L;
        if (paginationOptions.getRefresh()) {
            entries = productMapper.getTotalEntries(query, userId, isDeleted);
        }
        return new PaginationResponse<>(entries, products);
    }

    @Override
    public Product createProduct(ProductDTO product, ExtendedUserDetails userDetails) {
        Long newId = productMapper.insert(userDetails.getId(), product);
        return productMapper.find(newId);
    }

    public void checkOwnershipAndRun(Action action, Long productId, ExtendedUserDetails userDetails) {
        Product product = productMapper.find(productId);
        if (product == null) {
            throw new ResourceNotFoundException("The requested product was not found.");
        }

        if (!roleChecker.hasAdminRole(userDetails) && !userDetails.getId().equals(product.getUserId())) {
            throw new AccessDeniedException("You must be the owner of this product to perform this operation.");
        }

        action.execute();
    }

    @Override
    public void updateProduct(Long productId, ProductDTO product, ExtendedUserDetails userDetails) {
        checkOwnershipAndRun(() -> productMapper.update(productId, product), productId, userDetails);
    }

    @Override
    public void deleteProduct(Long productId, ExtendedUserDetails userDetails) {
        checkOwnershipAndRun(() -> productMapper.delete(productId), productId, userDetails);
    }

    @Override
    public void clearDeletedProducts() {
        productMapper.clearDeleted();
    }

    @FunctionalInterface
    public interface Action {
        void execute();
    }
}
