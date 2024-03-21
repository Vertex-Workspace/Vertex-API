package com.vertex.vertex.file;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.repository.FileRepository;
import com.vertex.vertex.file.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blob")
@AllArgsConstructor
public class FileController {
    private FileRepository fileRepository;

    @GetMapping("/{id}")
    public File getFileById(@PathVariable Long id){
        return fileRepository.findById(id).get();
    }
}
