package org.pan.config;

import com.google.common.collect.Multimap;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Part;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

public class MinIOClientEx extends MinioAsyncClient {
    public MinIOClientEx(MinioAsyncClient client) {
        super(client);
    }
    @Override
    public CompletableFuture<CreateMultipartUploadResponse> createMultipartUploadAsync(String bucketName, String region, String objectName, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) throws InsufficientDataException, InternalException, InvalidKeyException, IOException, NoSuchAlgorithmException, XmlParserException {
        return super.createMultipartUploadAsync(bucketName, region, objectName, headers, extraQueryParams);
    }
    @Override
    public CompletableFuture<ObjectWriteResponse> completeMultipartUploadAsync(String bucketName, String region, String objectName, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, XmlParserException, InternalException {
        return super.completeMultipartUploadAsync(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams);
    }

    @Override
    public CompletableFuture<ListPartsResponse> listPartsAsync(String bucketName, String region, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, XmlParserException, InternalException {
        return super.listPartsAsync(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams);
    }

}
