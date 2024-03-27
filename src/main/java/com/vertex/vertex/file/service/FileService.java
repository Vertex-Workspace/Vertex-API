package com.vertex.vertex.file.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.model.FileSupporter;
import com.vertex.vertex.file.repository.FileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final Environment env;


//    public void saveImageAws(Long idChat, MultipartFile multipartFile) throws IOException {
//
//
//        File file = new File(multipartFile);
//        fileRepository.save(file);
//
//        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(keyID, keySecret);
//        S3Configuration s3Configuration = S3Configuration.builder().build();
//        s3Configuration.toBuilder();
//
//    }

    public String uploadFile(MultipartFile file) {
        String keyID = env.getProperty("aws.accessKeyId");
        String keySecret = env.getProperty("aws.secretAccessKey");
        String bucket = env.getProperty("aws.bucket");

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(keyID, keySecret);

        try (S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(String.valueOf(Region.US_EAST_1)))
                .build()) {

            if (!doesBucketExist(s3Client, bucket)) {
                return "false";
            }


            String contentType = file.getContentType();

            String uuid = UUID.randomUUID().toString();


            try (InputStream fileInputStream = file.getInputStream()) {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(uuid)
                        .contentType(contentType)
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileInputStream, file.getSize()));
                return uuid;
            } catch (IOException e) {
                e.printStackTrace();
                return "false";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    public String getFileFromAWS(String name) throws Exception {
        System.out.println(name+"NAME SERVICE");
        String keyID = env.getProperty("aws.accessKeyId");
        String keySecret = env.getProperty("aws.secretAccessKey");
        String bucket = env.getProperty("aws.bucket");

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(keyID, keySecret);

        try(S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(String.valueOf(Region.US_EAST_1)))
                .build()){

            if (doesBucketExist(s3Client, bucket)) {
                try (S3Presigner presigner = S3Presigner.builder().region(Region.US_EAST_1).credentialsProvider(StaticCredentialsProvider.create(awsCredentials)).build()) {


                    GetObjectRequest objectRequest = GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(name)
                            .build();

                    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                            .getObjectRequest(objectRequest)
                            .build();

                    PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

                    return presignedRequest.url().toExternalForm();
                }
            }
            else return null;

        }catch (Exception e){
            throw new Exception(e);
        }
    }

    private boolean doesBucketExist(S3Client s3Client, String bucketName) {
        try {
            s3Client.headBucket(b -> b.bucket(bucketName));
            return true;
        } catch (S3Exception e) {
            return false;
        }
    }


    public File save(MultipartFile multipartFile, FileSupporter item) {
        try {
            File file = new File(multipartFile, item);
            return fileRepository.save(file);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    public File save(MultipartFile multipartFile) {
        try {
            File file = new File(multipartFile);
            return fileRepository.save(file);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    public void delete(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        fileRepository.deleteById(id);
    }

    public File findById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

}