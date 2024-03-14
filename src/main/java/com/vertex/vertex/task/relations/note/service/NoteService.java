package com.vertex.vertex.task.relations.note.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.repository.FileRepository;
import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.task.relations.note.model.dto.NoteDTO;
import com.vertex.vertex.task.relations.note.model.dto.NoteEditDTO;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import com.vertex.vertex.task.relations.note.repository.NoteRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserTeamService userTeamService;
    private final ProjectService projectService;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    public Note create(NoteDTO dto, Long projectId, Long userId) {
        Project project = projectService.findById(projectId);
        UserTeam creator = userTeamService
                .findUserTeamByComposeId
                        (userId, project.getTeam().getId());

        Note note = new Note(project, creator, dto);
        return noteRepository.save(note);
    }

    public Note findById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Note editPatch(NoteEditDTO dto) {
        Note note = findById(dto.getId());
        modelMapper.map(dto, note);
        return noteRepository.save(note);
    }

    public Note uploadFile(Long noteId, MultipartFile multipartFile) {
        try {
            Note note = findById(noteId);
            File file = new File(multipartFile, note);
            fileService.save(file, note);
            return noteRepository.save(note);

        } catch (Exception ignored) {
            throw new RuntimeException();
        }
    }

    public List<NoteDTO> findAllByProject(Long projectId) {
        return noteRepository
                .findAllByProject_Id(projectId)
                .stream()
                .map(NoteDTO::new)
                .toList();
    }

    public void delete(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        noteRepository.deleteById(id);
    }

    public Note removeImage(Long noteId, Long fileId) {
        Note note = findById(noteId);
        File file = fileService.findById(fileId);
        fileService.delete(fileId);
        note.getFiles().remove(file);
        return noteRepository.save(note);
    }

}
