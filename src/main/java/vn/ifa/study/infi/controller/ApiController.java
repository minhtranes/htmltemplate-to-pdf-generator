package vn.ifa.study.infi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.infi.model.RemoteFile;
import vn.ifa.study.infi.properties.ObjectStoreProperties;
import vn.ifa.study.infi.service.HtmlDocumentService;
import vn.ifa.study.infi.service.PdfGeneratorService;
import vn.ifa.study.oo.ObjectStorage;
import vn.ifa.study.oo.StoredObject;

@Slf4j
@RestController
@RequestMapping("api")
public class ApiController {

    @Autowired
    private HtmlDocumentService documentService;

    @Autowired
    private PdfGeneratorService generatorService;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ObjectStoreProperties templateStoreProperties;

    @PostMapping("{template}")
    public RemoteFile generate(
        @PathVariable(name = "template", required = true) final String template,
        @RequestBody final JsonNode request)
            throws IOException, InvalidKeyException, ErrorResponseException, InsufficientDataException,
            InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException {

        String requestId = request.findValue("requestId")
                .textValue();

        if ((requestId == null) || requestId.isEmpty()) {
            log.error("Content must be a valid id. A random UUID was used instead");
            requestId = UUID.randomUUID()
                    .toString();
        }

        String bucket = request.findValue("bucket")
                .textValue();
        String filePath = request.findValue("filePath")
                .textValue();

        String filename = null;

        if (filePath == null) {
            filePath = requestId + ".pdf";
            filename = filePath;
            log.warn("Missing filePath from request. Generated {}", filePath);
        } else {
            filename = filePath.substring(filePath.lastIndexOf("/"));
        }

        ObjectNode variableNode = (ObjectNode) request.findValue("variables");
        Map<String, JsonNode> variables = mapper.convertValue(variableNode, new TypeReference<Map<String, JsonNode>>() {
        });

        log.info("Generate pdf file for content id [{}] to file [{}]", requestId, filename);

        String templateUrl = String
                .join("/", templateStoreProperties.getBucket(), templateStoreProperties.getKeyPrefix(), template);
        String htmlDocument = documentService.generate(templateUrl, variables);
        File pdfFile = generatorService.generate(htmlDocument, filePath);
        long fileSize = Files.size(pdfFile.toPath());

        StoredObject so = StoredObject.builder()
                .bucket(bucket)
                .key(filePath)
                .size(fileSize)
                .build();
        ObjectStorage.putObject(so, new FileInputStream(pdfFile));

        return RemoteFile.builder()
                .url(String.join("/", so.getBucket(), so.getKey()))
                .filename(filename)
                .extension(".pdf")
                .size(fileSize)
                .build();

    }
}
