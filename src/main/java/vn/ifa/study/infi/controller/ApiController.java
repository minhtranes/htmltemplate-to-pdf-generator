package vn.ifa.study.infi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.infi.model.RemoteFile;
import vn.ifa.study.infi.service.HtmlDocumentService;
import vn.ifa.study.infi.service.ObjectStorageService;
import vn.ifa.study.infi.service.PdfGeneratorService;

@Slf4j
@RestController
@RequestMapping("api")
public class ApiController {

    @Autowired
    private HtmlDocumentService documentService;

    @Autowired
    private PdfGeneratorService generatorService;

    @Autowired
    private ObjectStorageService storageService;

    @PostMapping("{template}")
    public RemoteFile generate(
        @PathVariable(name = "template", required = true) final String template,
        @RequestBody final JsonNode content)
            throws IOException, InvalidKeyException, ErrorResponseException, InsufficientDataException,
            InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException {

        log.info("{}", content.toString());
        String contentId = content.findValue("id")
                .textValue();

        if ((contentId == null) || content.isEmpty()) {
            log.error("Content must be a valid id. A random UUID was used instead");
            contentId = UUID.randomUUID()
                    .toString();
        }

        String filename = contentId + ".pdf";
        log.info("Generate pdf file for content id [{}] to file [{}]", contentId, filename);

        String htmlDocument = documentService.generate(template, content);
        File pdfFile = generatorService.generate(htmlDocument, template, filename);
        long fileSize = Files.size(pdfFile.toPath());

        return storageService.putObject(filename, new FileInputStream(pdfFile), fileSize);

    }
}
