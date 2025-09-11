package com.java.test.junior.service.loader;

import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Resource;
import lombok.RequiredArgsConstructor;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaderServiceImp implements LoaderService {

    private final WebClient webClient = WebClient.create();
    private final ProductMapper productMapper;
    private final DataSource dataSource;

    @Qualifier("storageServers")
    private final List<Resource> storageServers;
    private final RestTemplate restTemplate;

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

        return webClient.get()
                .uri(baseUrl + "/data/files")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();
    }

    @Override
    public void loadProducts(Integer resourceId, String fileName, ExtendedUserDetails userDetails) {
        String copyQuery = "COPY staging_product (name, price, description) FROM STDIN WITH (FORMAT csv, HEADER true)";
        load(copyQuery, resourceId, fileName, userDetails);
    }

    private void load(String copyQuery, Integer resourceId, String fileName, ExtendedUserDetails userDetails) {
        String baseUrl = storageServers.get(resourceId).getPath();
        String url = baseUrl + "/data/stream/" + fileName;

        try (
                InputStream inputStream = restTemplate.execute(
                        url,
                        HttpMethod.GET,
                        null,
                        clientHttpResponse -> clientHttpResponse.getBody()
                );

                Connection dataConn = dataSource.getConnection()
        ) {
            PGConnection pgConn = dataConn.unwrap(PGConnection.class);
            CopyManager copyManager = pgConn.getCopyAPI();

            copyManager.copyIn(copyQuery, inputStream);

            productMapper.copyStaging(userDetails.getId());

        } catch (Exception e) {
            throw new RuntimeException("Error while copying data: " + e.getMessage(), e);
        }
    }

}

