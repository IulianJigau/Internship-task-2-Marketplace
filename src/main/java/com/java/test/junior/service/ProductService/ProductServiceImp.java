package com.java.test.junior.service.ProductService;

import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.model.RequestResponses.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
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
    private final HttpServletResponse httpServletResponse;

    private boolean isAdmin(ExtendedUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public ResponseEntity<?> getProductById(Long productId) {
        try {
            Product product = productMapper.find(productId);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse("The requested product was not found.")
                );
            }
            if(product.getIsDeleted()){
                return ResponseEntity.status(HttpStatus.GONE).body(
                        new ErrorResponse("The requested product has been deleted.")
                );
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> getProductsPage(Integer page, Integer size, String query) {
        try {
            List<Product> products = productMapper.getPage(page, size, query);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> getProductsPageByUserId(Integer page, Integer size, String query, Long userId) {
        try {
            List<Product> products = productMapper.getPageByUserId(page, size, query, userId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> getDeletedProductsPage(Integer page, Integer size, String query) {
        try {
            List<Product> products = productMapper.getDeletedPage(page, size, query);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> createProduct(ProductDTO product, ExtendedUserDetails userDetails) {
        try {
            productMapper.insert(userDetails.getId(), product);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> checkOwnershipAndRun(Action action, Long productId, ExtendedUserDetails userDetails) {
        try {
            Product product = productMapper.find(productId);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse("The requested product was not found.")
                );
            }

            if (!isAdmin(userDetails) && !userDetails.getId().equals(product.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new ErrorResponse("You must be the owner of this product to perform this operation.")
                );
            }

            action.execute();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> updateProduct(Long productId, ProductDTO product, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(() -> productMapper.update(productId, product), productId, userDetails);
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long productId, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(() -> productMapper.delete(productId), productId, userDetails);
    }

    @Override
    public ResponseEntity<?> clearDeletedProducts() {
        try {
            productMapper.clearDeleted();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @FunctionalInterface
    public interface Action {
        void execute() throws Exception;
    }
}
