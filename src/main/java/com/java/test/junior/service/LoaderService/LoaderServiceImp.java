package com.java.test.junior.service.LoaderService;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.model.RequestResponses.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaderServiceImp implements LoaderService {

    private static final Logger logger = LoggerFactory.getLogger(LoaderServiceImp.class);

    private final ProductMapper productMapper;

    private <T> List<T> loadCSV(String path, Class<T> type) throws Exception {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();

        try (InputStream stream = getClass().getResourceAsStream("/data/" + path)) {
            if (stream == null) {
                throw new FileNotFoundException("Resource not found: /data/" + path);
            }
            MappingIterator<T> iterator = mapper.readerFor(type).with(schema).readValues(stream);
            return iterator.readAll();
        }
    }

    @Override
    public ResponseEntity<?> loadProducts(ExtendedUserDetails userDetails) {
        final String path = "products.csv";
        final List<ProductDTO> products;

        try {
            products = loadCSV(path, ProductDTO.class);
        } catch (Exception e) {
            logger.error("Failed to load products from CSV: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse("The input file was not found.")
            );
        }

        try {
            for (ProductDTO product : products) {
                productMapper.insert(userDetails.getId(), product);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Failed to insert products: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
