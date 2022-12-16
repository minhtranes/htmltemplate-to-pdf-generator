package vn.ifa.study.infi.properties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ObjectStoreProperties {
    private String bucket;
    private boolean keyPrefixTemplate;
    private String keyPrefix;

}
