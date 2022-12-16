package vn.ifa.study.infi.controller;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.infi.properties.ObjectStoreProperties;
import vn.ifa.study.infi.service.HtmlDocumentService;
import vn.ifa.study.oo.ObjectStorage;
import vn.ifa.study.oo.StoredObject;

@Slf4j
@RestController
@RequestMapping("templates")
public class TemplateController {

    @Autowired
    private ObjectStoreProperties templateStoreProperties;

    @Autowired
    private ObjectStoreProperties testTemplateStoreProperties;

    @Autowired
    private HtmlDocumentService documentService;

    private final Map<String, JsonNode> testSamples = new HashMap<>();

    @PostMapping("import/{templateName}")
    public boolean importTemplate(
        @PathVariable(name = "templateName", required = true) final String templateName,
        @RequestParam(name = "folder", required = false) final String folder,
        @RequestBody final String content) {

        String url = folder == null ? String.join("/", templateStoreProperties.getKeyPrefix(), templateName)
                : String.join("/", templateStoreProperties.getKeyPrefix(), folder, templateName);
        StoredObject o = StoredObject.builder()
                .bucket(templateStoreProperties.getBucket())
                .key(url + ".html")
                .build();
        ObjectStorage.putObject(o, new ByteArrayInputStream(content.getBytes()));
        return true;
    }

    @PostMapping("importTestSamples/{variableName}")
    public String importTestSamples(
        @PathVariable(name = "variableName", required = true) final String variableName,
        @RequestBody final JsonNode sample) {

        testSamples.put(variableName, sample);
        String avSam = testSamples.keySet()
                .stream()
                .reduce((s1, s2) -> String.join("; ", s1, s2))
                .orElse("");
        return String.format("Variable [%s] was imported. Available variables are [%s]", variableName, avSam);
    }

    @PostMapping("test/{templateName}")
    public String testTemplate(
        @PathVariable(name = "templateName", required = true) final String templateName,
        @RequestBody final String content) {

        log.debug("TEMPLATE: {}", content);
        String templateUrl = String.join("/", testTemplateStoreProperties.getKeyPrefix(), templateName);
        byte[] bytes = content.getBytes();
        StoredObject o = StoredObject.builder()
                .bucket(testTemplateStoreProperties.getBucket())
                .key(templateUrl + ".html")
                .size(bytes.length)
                .build();
        ObjectStorage.putObject(o, new ByteArrayInputStream(bytes));

        if (testSamples.isEmpty()) {
            throw new IllegalStateException(
                    "There is no variable. Post variable to importTestSamples/{variableName} first");
        }

        log.info("Test to generate html from template {}", templateName);
        return documentService.generate(String.join("/", testTemplateStoreProperties.getBucket(), templateUrl),
                                        testSamples);
    }
}
