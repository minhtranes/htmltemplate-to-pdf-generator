package vn.ifa.study.infi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.ifa.study.infi.properties.PdfGeneratorProperties;

@Configuration
public class PdfGeneratorConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "pdf-generator")
    PdfGeneratorProperties pdfGeneratorProperties() {

        return new PdfGeneratorProperties();
    }
}
