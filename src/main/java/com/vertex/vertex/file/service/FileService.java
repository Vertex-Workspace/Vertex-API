package com.vertex.vertex.file.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.repository.FileRepository;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

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

}
