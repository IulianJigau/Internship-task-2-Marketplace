package com.java.test.junior.service.LoaderService;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.java.test.junior.exception.DatabaseFailException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaderServiceInsertImp implements LoaderService {

    private final ProductMapper productMapper;

    @Value("${app.product-file}")
    private String productFilePath;

    private <T> List<T> loadCSV(String path, Class<T> type) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();

        try (InputStream stream = getClass().getResourceAsStream("/data/" + path)) {
            if (stream == null) {
                throw new ResourceNotFoundException("Resource not found: /data/" + path);
            }
            MappingIterator<T> iterator = mapper.readerFor(type).with(schema).readValues(stream);
            return iterator.readAll();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public void loadProducts(ExtendedUserDetails userDetails) {
        final List<ProductDTO> products;

        products = loadCSV(productFilePath, ProductDTO.class);

        try {
            for (ProductDTO product : products) {
                productMapper.insert(userDetails.getId(), product);
            }
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
    }
}
