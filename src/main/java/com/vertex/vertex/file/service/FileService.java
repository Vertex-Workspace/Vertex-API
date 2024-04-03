package com.vertex.vertex.file.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.model.FileSupporter;
import com.vertex.vertex.file.repository.FileRepository;
import com.vertex.vertex.log.model.exception.EntityDoesntExistException;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

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
            throw new EntityDoesntExistException();
        }
        fileRepository.deleteById(id);
    }

    public File findById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

}
