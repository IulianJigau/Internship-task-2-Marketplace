package com.java.test.junior.service.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.exception.RequestFailException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Resource;
import com.java.test.junior.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaderServiceImp implements LoaderService {

    private final ProductService productService;
    private final DataSource dataSource;

    @Qualifier("storageServers")
    private final List<Resource> storageServers;
    private final RestTemplate restTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Resource> getResources() {
        return storageServers;
    }

    @Override
    public List<String> getResourceFiles(Integer resourceId) {
        boolean exists = storageServers.stream()
                .anyMatch(server -> server.getId().equals(resourceId));

        if (!exists) {
            throw new ResourceNotFoundException("The requested resource was not found.");
        }

        String baseUrl = storageServers.get(resourceId).getPath();
        String url = baseUrl + "/data/files";

        try {
            return restTemplate.execute(
                    url,
                    HttpMethod.GET,
                    null,
                    response -> {
                        try (InputStream inputStream = response.getBody()) {
                            return objectMapper.readValue(inputStream, new TypeReference<>() {
                            });
                        }
                    }
            );
        } catch (Exception e) {
            throw new RequestFailException(e.getMessage());
        }
    }

    @Override
    public void loadProducts(Integer resourceId, String fileName, ExtendedUserDetails userDetails) {
        String copyQuery = "COPY staging_product (name, price, description) FROM STDIN WITH (FORMAT csv, HEADER true)";
        load(copyQuery, resourceId, fileName);
        productService.copyStagingProducts(userDetails.getId());
    }

    private void load(String copyQuery, Integer resourceId, String fileName) {
        String baseUrl = storageServers.get(resourceId).getPath();
        String url = baseUrl + "/data/stream/" + fileName;

        try (Connection dataConn = dataSource.getConnection()) {
            PGConnection pgConn = dataConn.unwrap(PGConnection.class);
            CopyManager copyManager = pgConn.getCopyAPI();

            restTemplate.execute(
                    url,
                    HttpMethod.GET,
                    null,
                    response -> {
                        try (InputStream inputStream = response.getBody()) {
                            copyManager.copyIn(copyQuery, inputStream);
                            return null;
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
            );
        } catch (Exception e) {
            throw new RequestFailException(e.getMessage());
        }
    }
}

