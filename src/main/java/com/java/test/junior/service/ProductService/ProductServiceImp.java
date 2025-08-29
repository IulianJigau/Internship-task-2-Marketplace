package com.java.test.junior.service.ProductService;

import com.java.test.junior.exception.DatabaseFailException;
import com.java.test.junior.exception.ResourceDeletedException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductMapper productMapper;

    private boolean isAdmin(ExtendedUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public Product getProductById(Long productId) {
        try {
            Product product = productMapper.find(productId);
            if (product == null) {
                throw new ResourceNotFoundException("The requested product was not found.");
            }
            if (product.getIsDeleted()) {
                throw new ResourceDeletedException("The requested product was deleted.");
            }
            return product;
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
    }

    @Override
    public PaginationResponse<Product> getProductsPage(Integer page, Integer size, String query, Long userId, Boolean isDeleted) {
        try {
            List<Product> products = productMapper.getPage(page, size, query, userId, isDeleted);
            Long entries = productMapper.getTotalEntries(query, userId, isDeleted);
            return new PaginationResponse<>(entries, products);
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
    }

    @Override
    public Product createProduct(ProductDTO product, ExtendedUserDetails userDetails) {
        try {
            Long newId = productMapper.insert(userDetails.getId(), product);
            return productMapper.find(newId);
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
    }

    public void checkOwnershipAndRun(Action action, Long productId, ExtendedUserDetails userDetails) {
        try {
            Product product = productMapper.find(productId);
            if (product == null) {
                throw new ResourceNotFoundException("The requested product was not found.");
            }

            if (!isAdmin(userDetails) && !userDetails.getId().equals(product.getUserId())) {
                throw new AccessDeniedException("You must be the owner of this product to perform this operation.");
            }

            action.execute();
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
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
        try {
            productMapper.clearDeleted();
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
    }

    @FunctionalInterface
    public interface Action {
        void execute();
    }
}
