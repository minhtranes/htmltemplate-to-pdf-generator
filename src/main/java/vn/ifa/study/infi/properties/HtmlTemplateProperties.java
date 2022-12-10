package vn.ifa.study.infi.properties;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HtmlTemplateProperties {
    private Map<String, String> variableMappings;
}
