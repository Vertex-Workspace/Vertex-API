package com.vertex.vertex.file.service;

import com.vertex.vertex.file.repository.FileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public void delete(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        fileRepository.deleteById(id);
    }

}
