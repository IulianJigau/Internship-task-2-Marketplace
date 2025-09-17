package com.java.test.junior.service.loader;

import com.java.test.junior.client.StorageFileClient;
import com.java.test.junior.client.model.Provider;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaderServiceImpl implements LoaderService {

    private final static String PRODUCT_COPY_QUERY =
            "COPY staging_product (name, price, description) FROM STDIN WITH (FORMAT csv, HEADER true)";
    private final ProductService productService;
    private final StorageFileClient storageFileClient;
    private final List<Provider> providers;

    @Override
    public List<Provider> getProviders() {
        return providers;
    }

    @Override
    public List<String> getProviderFiles(Integer resourceId) {
        if (providerIsAbsent(resourceId)) {
            throw new ResourceNotFoundException("The requested resource was not found.");
        }

        String baseUrl = providers.get(resourceId).getPath();
        return storageFileClient.listFiles(baseUrl);
    }

    @Override
    public void loadProducts(Integer resourceId, String fileName, ExtendedUserDetails userDetails) {
        load(resourceId, fileName, PRODUCT_COPY_QUERY);
        productService.copyStagingProducts(userDetails.getId());
    }

    private void load(Integer resourceId, String fileName, String query) {
        if (providerIsAbsent(resourceId)) {
            throw new ResourceNotFoundException("The requested resource was not found.");
        }

        String baseUrl = providers.get(resourceId).getPath();

        storageFileClient.loadFile(query, baseUrl, fileName);
    }

    private boolean providerIsAbsent(Integer resourceId) {
        return providers.stream()
                .noneMatch(server -> server.getId().equals(resourceId));
    }
}

