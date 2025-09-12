package com.java.test.junior.service.loader;

import com.java.test.junior.client.StorageFileClient;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Resource;
import com.java.test.junior.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaderServiceImp implements LoaderService {

    private final static String PRODUCT_COPY_QUERY =
            "COPY staging_product (name, price, description) FROM STDIN WITH (FORMAT csv, HEADER true)";
    private final ProductService productService;
    private final StorageFileClient storageFileClient;
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

        String baseUrl = storageServers.get(resourceId).getPath();

        if (exists) {
            return storageFileClient.listFiles(baseUrl);
        }

        throw new ResourceNotFoundException("The requested resource was not found.");

    }

    @Override
    public void loadProducts(Integer resourceId, String fileName, ExtendedUserDetails userDetails) {
        String baseUrl = storageServers.get(resourceId).getPath();

        storageFileClient.loadFile(PRODUCT_COPY_QUERY, baseUrl, fileName);
        productService.copyStagingProducts(userDetails.getId());
    }
}

