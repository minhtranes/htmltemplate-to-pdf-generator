package vn.ifa.study.infi.service;

import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.infi.config.ObjectStorageTemplateResolver;

@Slf4j
@Service
public class HtmlDocumentService {

    private TemplateEngine templateEngine;

    public String generate(final String template, final Map<String, JsonNode> variables) {

        if ((variables == null) || (variables.size() <= 0)) {
            log.warn("No declared variable found");
            throw new IllegalArgumentException("Request must contain a valid variables");
        }

        Context context = new Context();
        variables.entrySet()
                .forEach(e -> context.setVariable(e.getKey(), e.getValue()));

        return templateEngine.process(template, context);
    }

    @PostConstruct
    void initialize() {

        ObjectStorageTemplateResolver objectStorageTemplateResolver = new ObjectStorageTemplateResolver();
        objectStorageTemplateResolver.setSuffix(".html");
        objectStorageTemplateResolver.setTemplateMode(TemplateMode.HTML);
        objectStorageTemplateResolver.setResolvablePatterns(Set.of("*"));

        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(objectStorageTemplateResolver);
        templateEngine.addDialect(new Java8TimeDialect());
    }

}
