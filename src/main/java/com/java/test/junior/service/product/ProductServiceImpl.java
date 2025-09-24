package com.java.test.junior.service.product;

import com.java.test.junior.exception.ResourceDeletedException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.product.Product;
import com.java.test.junior.model.product.ProductDTO;
import com.java.test.junior.model.response.PaginationResponse;
import com.java.test.junior.service.CacheService;
import com.java.test.junior.service.user.UserService;
import com.java.test.junior.util.Procedure;
import com.java.test.junior.util.RoleChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final UserService userService;
    private final RoleChecker roleChecker;
    private final CacheService cacheService;

    @Override
    public Boolean existsProductId(Long id) {
        return productMapper.exists(id);
    }

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
        if (userId == null) {
            return getProductPaginationResponse(paginationOptions, query, null, isDeleted);
        }

        if (userService.existsUserId(userId)) {
            return getProductPaginationResponse(paginationOptions, query, userId, isDeleted);
        }

        throw new ResourceNotFoundException("The requested user was not found.");
    }

    private PaginationResponse<Product> getProductPaginationResponse(PaginationOptionsDTO paginationOptions, String query, Long userId, Boolean isDeleted) {
        List<Product> products = productMapper.getPage(
                paginationOptions.getPage(),
                paginationOptions.getPageSize(),
                query,
                userId,
                isDeleted
        );

        long entries = -1L;
        if (paginationOptions.getRefresh()) {
            entries = productMapper.getTotalEntries(query, userId, isDeleted);
        }

        return new PaginationResponse<>(entries, products);
    }

    @Override
    public Product createProduct(ProductDTO product, ExtendedUserDetails userDetails) {
        Long newId = productMapper.insert(userDetails.getId(), product);
        return getProductById(newId);
    }

    public void checkOwnershipAndRun(Procedure procedure, Long productId, ExtendedUserDetails userDetails) {
        Product product = getProductById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("The requested product was not found.");
        }

        if (roleChecker.hasAdminRole(userDetails) || userDetails.getId().equals(product.getUserId())) {
            procedure.execute();
            return;
        }

        throw new AccessDeniedException("You must be the owner of this product to perform this operation.");
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

    @Override
    public void copyStagingProducts(Long userId) {
        productMapper.copyStaging(userId);
    }

    @Override
    public Product scrollProducts(String query, Boolean refresh, ExtendedUserDetails userDetails) {
        String key = userDetails.getId() + "&products&" + query;

        if (refresh) {
            cacheService.refreshCache(key);
        }

        Product product = cacheService.popItem(key, Product.class);
        if (product != null) {
            return product;
        }

        int nextPage = cacheService.getPageCount(key) + 1;
        List<Product> products = productMapper.getPage(nextPage, 10, query, userDetails.getId(), false);

        if (products.isEmpty()) {
            return null;
        }

        cacheService.pushItems(key, products);
        return cacheService.popItem(key, Product.class);
    }

}