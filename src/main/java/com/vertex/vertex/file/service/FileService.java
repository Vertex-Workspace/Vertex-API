package com.vertex.vertex.file.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.repository.FileRepository;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class FileService {

    private final FileRepository fileRepository;
    private final ProjectRepository projectRepository;
    private final Environment environment;

    public void delete(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        fileRepository.deleteById(id);
    }

    public void updateImageProject(MultipartFile file, Long projectId) throws IOException {

            //keys and buckets - aws configuration
            String key = environment.getProperty("keyID");
            String secretKey = environment.getProperty("keySecret");
            String bucketName = environment.getProperty("bucket");
            String fileKey = UUID.randomUUID().toString();

            AwsBasicCredentials awsBasicCredentials
                    = AwsBasicCredentials.create(key, secretKey);

            try (S3Client s3Client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                    .region(Region.of("us-east-1"))
                    .build()) {

                try (InputStream fileInputStream = file.getInputStream()) {
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileKey)
                            .contentType(file.getContentType())
                            .build();

                    s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileInputStream, file.getSize()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch (Exception e) {
                e.getMessage();
            }

            File fileEntity = new File(file);
            fileEntity.setAwsKey(fileKey);

            Project project = projectRepository.findById(projectId).get();
            fileEntity.setImage(file.getBytes());
            fileRepository.save(fileEntity);
            project.setFile(fileEntity);
            //this setImage can be dropped if the aws integration is done
            project.setImage(file.getBytes());
            projectRepository.save(project);
    }

}
