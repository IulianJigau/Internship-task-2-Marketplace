package com.java.test.junior.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.exception.RequestFailException;
import com.java.test.junior.exception.ResourceValidationException;
import lombok.RequiredArgsConstructor;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.java.test.junior.model.response.ErrorResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class StorageFileClientImpl implements StorageFileClient{

    private final static String DATA_PATH = "/data";
    private final static String FILES_PATH = "/files";
    private final static String STREAM_PATH = "/stream";

    private final RestTemplate restTemplate;
    private final DataSource dataSource;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<String> listFiles(String baseURL) {
        String url = baseURL + DATA_PATH + FILES_PATH;

        return executeRequest(stringListDeserializer(), url);
    }

    private Function<InputStream, List<String>> stringListDeserializer() {
        return inputStream -> {
            try {
                return objectMapper.readValue(inputStream, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                throw new RequestFailException(e.getMessage());
            }
        };
    }

    @Override
    public void loadFile(String copyQuery, String baseUrl, String fileName) {
        String url = baseUrl + DATA_PATH + STREAM_PATH + "/" + fileName;
        Function<InputStream, Void> streamHandler;

        try (Connection dataConn = dataSource.getConnection()) {
            PGConnection pgConn = dataConn.unwrap(PGConnection.class);
            CopyManager copyManager = pgConn.getCopyAPI();
            streamHandler = executeCopy(copyManager, copyQuery);
        } catch (Exception e) {
            throw new RequestFailException(e.getMessage());
        }

            executeRequest(streamHandler, url);
    }

    private Function<InputStream, Void> executeCopy(CopyManager copyManager, String copyQuery) {
        return inputStream -> {
            try {
                copyManager.copyIn(copyQuery, inputStream);
                return null;
            } catch (Exception e) {
                throw new RequestFailException(e.getMessage());
            }
        };
    }

    private <Type> Type executeRequest(Function<InputStream, Type> function, String url) {
        return restTemplate.execute(
                url,
                HttpMethod.GET,
                null,
                response -> {
                    try (InputStream inputStream = response.getBody()) {
                        return function.apply(inputStream);
                    }
                }
        );
    }
}
