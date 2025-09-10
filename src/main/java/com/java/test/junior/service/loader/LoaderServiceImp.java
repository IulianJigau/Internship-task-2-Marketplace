package com.java.test.junior.service.loader;

import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.exception.ResourceValidationException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Resource;
import lombok.RequiredArgsConstructor;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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

    public void load(String copyQuery, Integer resourceId, String fileName, ExtendedUserDetails userDetails) {
        String baseUrl = storageServers.get(resourceId).getPath();
        URL url;
        HttpURLConnection serverConn;

        try {
            url = URI.create(baseUrl + "/data/stream/" + fileName).toURL();
            serverConn = (HttpURLConnection) url.openConnection();
            serverConn.setRequestMethod("GET");
        } catch (Exception e) {
            throw new ResourceValidationException("The server you are trying to reach is not valid.");
        }

        try (InputStream inputStream = serverConn.getInputStream();
             Connection dataConn = dataSource.getConnection()) {
            PGConnection pgConn = dataConn.unwrap(PGConnection.class);
            CopyManager copyManager = pgConn.getCopyAPI();

            copyManager.copyIn(copyQuery, inputStream);
            productMapper.copyStaging(userDetails.getId());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

