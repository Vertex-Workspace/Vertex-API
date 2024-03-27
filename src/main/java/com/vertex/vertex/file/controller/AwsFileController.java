package com.vertex.vertex.file.controller;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.service.FileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;

@RestController
@RequestMapping("/awsFile")
@AllArgsConstructor
@Data
public class AwsFileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<?> postAwsS3(@RequestParam MultipartFile file){
        try {

            return new ResponseEntity<>(fileService.uploadFile(file),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getAwsFile/{name}")
    public ResponseEntity<String> getAwsFile(@PathVariable String name){
        try {
            System.out.println(name+"NAME");
            return new ResponseEntity<>(fileService.getFileFromAWS(name),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}