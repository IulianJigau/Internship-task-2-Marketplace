package com.java.test.junior.service.loader;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Resource;

import java.io.InputStream;
import java.util.List;

public interface LoaderService {
    List<Resource> getResources();

    void loadProducts(Integer resourceId, String fileName, ExtendedUserDetails userDetails);

    List<String> getResourceFiles(Integer resourceId);
}
