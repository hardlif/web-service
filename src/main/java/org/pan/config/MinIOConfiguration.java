package org.pan.config;

import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import io.minio.S3Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfiguration {
    private final MinIOCustomProperties minIOCustomProperties;

    public MinIOConfiguration(MinIOCustomProperties minIOCustomProperties) {
        this.minIOCustomProperties = minIOCustomProperties;
    }
    @Bean
    public MinIOClientEx minioClient(){
        MinioAsyncClient minioAsyncClient = MinIOClientEx.builder()
                .endpoint(minIOCustomProperties.getEndpoint())
                .credentials(minIOCustomProperties.getAccessKey(), minIOCustomProperties.getSecretKey())
                .build();

        return new MinIOClientEx( minioAsyncClient);
    }
}
