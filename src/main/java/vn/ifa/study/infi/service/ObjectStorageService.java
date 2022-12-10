package vn.ifa.study.infi.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import vn.ifa.study.infi.model.RemoteFile;
import vn.ifa.study.infi.properties.ObjectStorageProperties;

@Service
public class ObjectStorageService {

    private final static Object locker = new Object();

    private MinioClient minioInstance;

    @Autowired
    private ObjectStorageProperties props;

    private MinioClient minioClient() {

        synchronized (locker) {

            if (minioInstance == null) {
                minioInstance = MinioClient.builder()
                        .endpoint(props.getEndpoint())
                        .credentials(props.getClientKey(), props.getClientSecret())
                        .build();
            }

            return minioInstance;
        }

    }

    public RemoteFile putObject(final String name, final InputStream is, final long size)
            throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
            InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IOException {

        return putObject(props.getDefaultBucket(), name, is, size);
    }

    public RemoteFile putObject(final String bucket, final String name, final InputStream is, final long size)
            throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
            InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IOException {

        PutObjectArgs oa = PutObjectArgs.builder()
                .stream(is, size, -1)
                .bucket(bucket)
                .object(name)
                .build();
        minioClient().putObject(oa);
        return RemoteFile.builder()
                .filename(name)
                .url(String.join("/", bucket, name))
                .size(size)
                .build();
    }
}
