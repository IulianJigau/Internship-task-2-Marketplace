package com.java.test.junior.service.LoaderService;

import com.java.test.junior.exception.DatabaseFailException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Primary
public class LoaderServiceCopyImp implements LoaderService {

    private final ProductMapper productMapper;

    @Value("${app.product-file}")
    private String productFilePath;

    private File csvToTemp(String fileName) {
        String resourcePath = "/data/" + fileName;
        ClassPathResource resource = new ClassPathResource(resourcePath);

        if (!resource.exists()) {
            throw new ResourceNotFoundException("Resource not found: " + resourcePath);
        }

        File tempFile = new File("C:/Temp/products.csv");

        try (InputStream in = resource.getInputStream();
             FileOutputStream out = new FileOutputStream(tempFile)) {
            in.transferTo(out);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Failed to extract CSV to server path: " + e.getMessage());
        }

        return tempFile;
    }

    @Override
    public void loadProducts(ExtendedUserDetails userDetails) {
        try {
            File tempFile = csvToTemp(productFilePath);
            productMapper.bulkImport(tempFile.getAbsolutePath(), userDetails.getId());
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
    }
}
