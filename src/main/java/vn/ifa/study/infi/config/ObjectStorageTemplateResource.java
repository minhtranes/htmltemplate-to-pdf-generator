package vn.ifa.study.infi.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;

import org.thymeleaf.templateresource.ITemplateResource;

import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.infi.service.ObjectStorageService;

@Slf4j
public class ObjectStorageTemplateResource implements ITemplateResource, Serializable {
    private static final long serialVersionUID = 786859883149542189L;
    private final ObjectStorageService objectStorageService;
    private final String bucket;
    private final String key;

    public ObjectStorageTemplateResource(final ObjectStorageService objectStorageService, final String bucket,
            final String key) {

        this.objectStorageService = objectStorageService;
        this.bucket = bucket;
        this.key = key;
    }

    @Override
    public boolean exists() {

        try {
            return objectStorageService.bucketExists(bucket);
        }
        catch (Exception e) {
            log.error("Exception when checking bucket existence {}", bucket, e);
            return false;
        }

    }

    @Override
    public String getBaseName() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDescription() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Reader reader() throws IOException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ITemplateResource relative(final String relativeLocation) {

        // TODO Auto-generated method stub
        return null;
    }

}
