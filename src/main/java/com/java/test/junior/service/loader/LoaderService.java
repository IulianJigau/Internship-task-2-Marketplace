package com.java.test.junior.service.loader;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Provider;

import java.util.List;

public interface LoaderService {
    List<Provider> getProviders();

    void loadProducts(Integer resourceId, String fileName, ExtendedUserDetails userDetails);

    List<String> getProviderFiles(Integer resourceId);
}
