package vn.ifa.study.infi.config;

import java.util.Map;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import vn.ifa.study.infi.service.ObjectStorageService;

public class ObjectStorageTemplateResolver extends AbstractConfigurableTemplateResolver {

    private final ObjectStorageService objectStorageService;

    public ObjectStorageTemplateResolver(final ObjectStorageService objectStorageService, final String bucket) {

        this.objectStorageService = objectStorageService;

    }

    @Override
    protected ITemplateResource computeTemplateResource(
        final IEngineConfiguration configuration,
        final String ownerTemplate,
        final String template,
        final String resourceName,
        final String characterEncoding,
        final Map<String, Object> templateResolutionAttributes) {

        String bucket = resourceName.substring(0, resourceName.indexOf("/"));
        String key = resourceName.substring(resourceName.indexOf("/"));

        return new ObjectStorageTemplateResource(objectStorageService, bucket, key);
    }

}
