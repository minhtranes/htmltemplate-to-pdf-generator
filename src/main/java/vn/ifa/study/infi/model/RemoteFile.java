package vn.ifa.study.infi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RemoteFile {
    private String filename;
    private String url;
    private String extension;
    private String thumbnailUrl;
    private long size;
}
