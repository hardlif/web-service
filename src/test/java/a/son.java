package a;

import b.father;
import io.minio.MinioClient;

class son extends father{
    public static void main(String[] args) {
        MinioClient minioClient = MinioClient.builder().build();
//        new father().say();
    }
}
