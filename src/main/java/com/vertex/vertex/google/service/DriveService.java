package com.vertex.vertex.google.service;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class DriveService {

    public List<File> getItens() throws IOException, GeneralSecurityException {
        Drive service = DriveConfig.createDrive();

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
        return files;
    }

    public String createFolderAndGetID(Drive service, String folderName) throws IOException {
        // Check if the folder already exists
        FileList result = service.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "' and trashed=false")
                .setFields("files(id, name)")
                .execute();
        List<File> files = result.getFiles();

        if (files != null && !files.isEmpty()) {
            // Folder already exists, return the first found folder ID
            return files.get(0).getId();
        }

        // Folder doesn't exist, create it
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = service.files().create(fileMetadata)
                .setFields("id")
                .execute();
        System.out.println("Folder ID: " + file.getId());

        return file.getId();
    }

    public void setPermissionsToFile(Drive service, String fileId) throws IOException {

        JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {

            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                // Handle error
                System.err.println(e.getMessage());
            }

            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                System.out.println("Permission ID: " + permission.getId());
            }
        };
        BatchRequest batch = service.batch();
        Permission userPermission = new Permission()
                .setType("anyone")
                .setRole("reader")
                .setAllowFileDiscovery(false);
        service.permissions().create(fileId, userPermission)
                .setFields("id")
                .queue(batch, callback);

        batch.execute();
    }

    public void uploadFilesAndGetURIs(MultipartFile fileMulti) throws IOException, GeneralSecurityException {
        Drive service = DriveConfig.createDrive();
        /* Create a folder and get Folder Id */
        String folderId = createFolderAndGetID(service, "Arquivos Vertex");

        /* Upload files to Google Drive */
        File fileMetadata = new File();
        fileMetadata.setName(fileMulti.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(folderId));

        // Convert MultipartFile to InputStreamContent
        InputStreamContent mediaContent = new InputStreamContent(fileMulti.getContentType(), fileMulti.getInputStream());

        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id, name, webContentLink, webViewLink")
                .execute();

        /* Set permissions to file */
        setPermissionsToFile(service, file.getId());

        /* Get file properties: Id, Name, Link */
        System.out.println("File Name: " + file.getName());
        System.out.println("File ID: " + file.getId());
        System.out.println("Download Link: " + file.getWebContentLink());
        System.out.println("View Link: " + file.getWebViewLink());
        System.out.println(" ---- ");
    }


    public void deleteFilesByName(String fileName) throws IOException, GeneralSecurityException {
        Drive service = DriveConfig.createDrive();

        FileList result = service.files().list()
                .setQ("name='" + fileName + "' and trashed=false")
                .setFields("files(id, name)")
                .execute();
        List<com.google.api.services.drive.model.File> files = result.getFiles();

        if (files != null && !files.isEmpty()) {
            for (com.google.api.services.drive.model.File file : files) {
                service.files().delete(file.getId()).execute();
                System.out.println("Arquivo excluído: " + file.getName());
            }
        } else {
            System.out.println("Nenhum arquivo encontrado com o nome: " + fileName);
        }
    }
}
