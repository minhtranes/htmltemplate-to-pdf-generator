package vn.ifa.study.infi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.ifa.study.infi.properties.HtmlTemplateProperties;

@Configuration
public class HtmlDocumentConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "template")
    HtmlTemplateProperties templateProperties() {

        return new HtmlTemplateProperties();
    }
}
