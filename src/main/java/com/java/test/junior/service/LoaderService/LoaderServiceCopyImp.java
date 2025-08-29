package com.java.test.junior.service.LoaderService;

import com.java.test.junior.exception.DatabaseFailException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class LoaderServiceCopyImp implements LoaderService {

    private final ProductMapper productMapper;

    @Value("${app.product-file}")
    private String productFilePath;

    @Override
    public void loadProducts(ExtendedUserDetails userDetails) {
        try {
            productMapper.bulkImport(productFilePath, userDetails.getId());
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
    }
}
