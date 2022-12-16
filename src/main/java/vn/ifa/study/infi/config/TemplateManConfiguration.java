package vn.ifa.study.infi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.ifa.study.infi.properties.ObjectStoreProperties;

@Configuration
public class TemplateManConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "templates.storage")
    ObjectStoreProperties templateStoreProperties() {

        return new ObjectStoreProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "templates.test-storage")
    ObjectStoreProperties testTemplateStoreProperties() {

        return new ObjectStoreProperties();
    }
}
