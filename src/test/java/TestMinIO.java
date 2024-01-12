import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Part;
import org.pan.config.MinIOClientEx;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestMinIO {
    // Create a minioClient with the MinIO server playground, its access key and secret key.
    static MinioClient minioClient =
            MinioClient.builder()
//                            .endpoint("https://play.min.io")
//                            .credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
                    .endpoint("http://192.168.1.37:9000")
                    .credentials("zhaowang", "zhaowang137")
                    .build();

    static MinIOClientEx minioClientEx =
            new MinIOClientEx(MinIOClientEx.builder()
                    .endpoint("http://192.168.1.37:9000")
                    .credentials("zhaowang", "zhaowang137")
                    .build());
    public static void main(String[] args)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException, InternalException, InterruptedException, ExecutionException {
//        upload();
//        download();
//        test1();
        CompletableFuture<ListPartsResponse> future = minioClientEx.listPartsAsync
                ("minio-upload", null, "2024-01-10/ec310e02-7d2e-4b35-8f43-68da75de50cc.zip", 1000, 0, "NGE4NWY2MjctYmNjMC00YjU2LTg4MjItMzAxZGUzNzU4ZGYyLjJjMTdiYjhkLWU3ZWEtNDRhMS04NGFjLWI0NGJiNjNmYzE4NQ", null, null);
        ListPartsResponse response = future.get();
        List<Part> partList = response.result().partList();
        System.out.println(partList);
    }



    /**
     * 只有分片上传获取不到数据和
     */
    private static void test1() {
        StatObjectResponse statObjectResponse = null;
        try {
            statObjectResponse = minioClient.statObject(StatObjectArgs.builder()
                    .bucket("minio-upload")
                    .object("2024-01-10/edca59dc-552c-4066-902a-d0c40e4f042d.zip")
                    .build());
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }
        System.out.println(isObjectExist("minio-upload","2024-01-10/edca59dc-552c-4066-902a-d0c40e4f042d.zip"));
    }

    private static void download() throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
            minioClient.downloadObject(
                DownloadObjectArgs.builder()
                        .bucket("asiatrip")
                        .object("one/two/屏幕截图 2023-12-26 230827.png")
                        .filename("C:\\Users\\zhao\\Desktop\\test.png")
                        .build());
    }

    private static void upload() throws InvalidKeyException, IOException, NoSuchAlgorithmException {
        try {


            // Make 'asiatrip' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("asiatrip").build());
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // Upload 'C:\Users\zhao\Desktop\nacos.bat' as object name 'nacos.bat' to bucket

            // 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
//                            .bucket("asiatrip")
//                            .object("asiaphotos-2015.zip")
//                            .filename("/home/user/Photos/asiaphotos.zip")
                            .bucket("asiatrip")
                            .object("nacos.bat")
                            .filename("C:\\Users\\zhao\\Desktop\\nacos.bat")
                            .build());
            System.out.println(
                    "'/home/user/Photos/asiaphotos.zip' is successfully uploaded as "
                            + "object 'asiaphotos-2015.zip' to bucket 'asiatrip'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }

    static boolean isObjectExist(String bucketName, String objectName) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

}

