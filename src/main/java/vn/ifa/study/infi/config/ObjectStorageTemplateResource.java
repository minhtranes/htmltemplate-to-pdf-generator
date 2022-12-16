package vn.ifa.study.infi.config;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;

import org.thymeleaf.templateresource.ITemplateResource;

import lombok.extern.slf4j.Slf4j;
import vn.ifa.study.oo.ObjectStorage;
import vn.ifa.study.oo.StoredObject;

@Slf4j
public class ObjectStorageTemplateResource implements ITemplateResource, Serializable {
    private static final long serialVersionUID = 786859883149542189L;
    private final String bucket;
    private final String key;

    public ObjectStorageTemplateResource(final String bucket, final String key) {

        this.bucket = bucket;
        this.key = key;
    }

    @Override
    public boolean exists() {

        try {
            return ObjectStorage.createBucket(bucket);
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

        StoredObject o = StoredObject.builder()
                .bucket(bucket)
                .key(key)
                .build();
        StoredObject object = ObjectStorage.getObject(o, null);

        if (object == null) {
            return BufferedReader.nullReader();
        }

        log.info("Get template {} from storage {}", object.getKey(), object.getBucket());

        byte[] smallContent = o.getSmallContent();

        InputStream inputStream = new ByteArrayInputStream(smallContent);

        return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream)));
    }

    @Override
    public ITemplateResource relative(final String relativeLocation) {

        // TODO Auto-generated method stub
        return null;
    }

}
