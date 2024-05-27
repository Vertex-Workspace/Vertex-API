package com.vertex.vertex.google.controller;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.util.Value;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.vertex.vertex.google.service.DriveConfig;
import com.vertex.vertex.google.service.DriveService;
import com.vertex.vertex.google.util.GoogleCredentials;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@RestController
@RequestMapping("/drive")
@AllArgsConstructor
@CrossOrigin
public class DriveController {


    private final DriveService driveService;


    @GetMapping("/getItens")
    public ResponseEntity<?> getItens(){
        try {
            return new ResponseEntity<>(driveService.getItens(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createFile")
    public ResponseEntity<?> createFile(@RequestParam MultipartFile file){
        try {
            System.out.println(file);
            driveService.uploadFilesAndGetURIs(file);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteFile/{name}")
    public ResponseEntity<?> deleteFile(@PathVariable String name){
        try {
            System.out.println(name);
            driveService.deleteFilesByName(name);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


//    @PostMapping("/upload/{id}")
//    public ResponseEntity<String> uploadFileToDrive(@RequestParam("file") MultipartFile file,@PathVariable Long id) {
//        try {
//            // Carrega as credenciais do arquivo JSON de credenciais do Google Drive
//            Credential credentials = DriveConfig.getCredentials(id);
//
//            // Inicializa o serviço do Google Drive
//            Drive driveService = DriveConfig.createDrive();
//
//            // Cria o arquivo no Google Drive
//            File fileMetadata = new File();
//            fileMetadata.setName(file.getOriginalFilename());
//            fileMetadata.setParents(Collections.singletonList("my-drive")); // Define a pasta onde o arquivo será enviado
//
//            InputStreamContent mediaContent = new InputStreamContent(file.getContentType(), file.getInputStream());
//
//            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
//                    .setFields("id")
//                    .execute();
//
//            // Retorna a resposta com o ID do arquivo criado no Google Drive
//            return new ResponseEntity<>(uploadedFile.getId(), HttpStatus.OK);
//        } catch (IOException | GeneralSecurityException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Erro ao enviar o arquivo para o Google Drive", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


}
