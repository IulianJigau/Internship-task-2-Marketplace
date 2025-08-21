package com.java.test.junior.service.ProductService;

import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
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

    private boolean isAdmin(ExtendedUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public ResponseEntity<?> getProductById(Long productId) {
        try {
            Product product = productMapper.findById(productId);
            return (product != null)
                    ? ResponseEntity.ok(product)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> getProductPage(Integer page, Integer size, String query) {
        try {
            List<Product> products = productMapper.getPage(page, size, query);
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
            Product product = productMapper.findById(productId);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            if (!isAdmin(userDetails) && !userDetails.getId().equals(product.getUserId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            action.execute();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> updateProduct(Long productId, ProductDTO productDTO, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(() -> productMapper.update(productId, productDTO), productId, userDetails);
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long productId, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(() -> productMapper.delete(productId), productId, userDetails);
    }

    @FunctionalInterface
    public interface Action {
        void execute() throws Exception;
    }
}
