package com.vertex.vertex.file.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.repository.FileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public File save(File file) {
        return fileRepository.save(file);
    }

    public void delete(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        fileRepository.deleteById(id);
    }

}
