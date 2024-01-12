package org.pan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinIOCustomProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
