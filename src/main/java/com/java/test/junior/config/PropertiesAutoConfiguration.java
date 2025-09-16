package com.java.test.junior.config;

import com.java.test.junior.client.component.StorageProviders;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(StorageProviders.class)
public class PropertiesAutoConfiguration {
}
