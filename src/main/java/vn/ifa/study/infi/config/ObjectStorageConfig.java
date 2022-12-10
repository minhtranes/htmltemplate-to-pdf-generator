package vn.ifa.study.infi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.ifa.study.infi.properties.ObjectStorageProperties;

@Configuration
public class ObjectStorageConfig {

    @Bean
    @ConfigurationProperties(prefix = "object-storage")
    ObjectStorageProperties objectStorageProperties() {

        return new ObjectStorageProperties();
    }
}
