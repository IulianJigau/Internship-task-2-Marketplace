package com.java.test.junior.client;

import java.util.List;

public interface StorageFileClient {

    List<String> listFiles(String baseURL);

    void loadFile(String copyQuery, String baseUrl, String fileName);
}
