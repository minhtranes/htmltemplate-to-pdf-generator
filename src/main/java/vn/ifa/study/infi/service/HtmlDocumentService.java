package vn.ifa.study.infi.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.infi.properties.HtmlTemplateProperties;

@Slf4j
@Service
public class HtmlDocumentService {

    private ClassLoaderTemplateResolver templateResolver;

    private TemplateEngine templateEngine;

    @Autowired
    private HtmlTemplateProperties templateProperties;

    public String generate(final String template, final Object data) {

        String variable = templateProperties.getVariableMappings()
                .get(template);

        if (variable == null) {
            variable = template;
            log.warn("There is no variable mapping for template [{}]. So, same name variable was used");
        }

        Context context = new Context();
        context.setVariable(variable, data);

        return templateEngine.process("templates/" + template, context);
    }

    @PostConstruct
    void initialize() {

        templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new Java8TimeDialect());
    }

}
