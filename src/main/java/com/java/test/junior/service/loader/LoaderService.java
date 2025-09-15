package com.java.test.junior.service.loader;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Resource;

import java.util.List;

public interface LoaderService {
    List<Resource> getProviders();

    void loadProducts(Integer resourceId, String fileName, ExtendedUserDetails userDetails);

    List<String> getProviderFiles(Integer resourceId);
}
