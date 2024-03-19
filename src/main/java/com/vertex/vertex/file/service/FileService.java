package com.vertex.vertex.file.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.repository.FileRepository;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    private final Environment environment;

    public File save(File file, Note note) {
        if (Objects.isNull(note.getFiles())) note.setFiles(List.of(file));
        else note.getFiles().add(file);
        return fileRepository.save(file);
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

    public File updateImageProject(MultipartFile file){
        String awsKeyId = environment.getProperty("aws.accessKeyId");
        String awsKeySecret = environment.getProperty("aws.secretAccessKey");
        String awsBucket = environment.getProperty("aws.bucket");
        String randomCode = UUID.randomUUID().toString();

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(awsKeyId, awsKeySecret);

        try (S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .region(Region.of(String.valueOf(Region.US_EAST_1)))
                .build()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsBucket)
                    .key(randomCode)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            return new File(file.getOriginalFilename(), randomCode);

        } catch (Exception e) {
            throw new RuntimeException("Error");
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

}
