package com.java.test.storage.service.DataRetriever;

import com.java.test.junior.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DataRetrieverImp implements DataRetriever {

    @Value("${app.storage-path}")
    private String storagePath;

    public String getCsv(String fileName) {
        Path csvPath = Path.of(storagePath, fileName);
        try {
            return Files.readString(csvPath);
        }
        catch (IOException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public String provideProducts(){
        return getCsv("products.csv");
    }
}
