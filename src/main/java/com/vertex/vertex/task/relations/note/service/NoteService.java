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

        Note note = new Note();

        note.setProject(project);
        dto.setDescription("Sem descrição.");
//        note.setProject(project);
        note.setCreator(creator);
        BeanUtils.copyProperties(dto, note);
        if (Objects.isNull(project.getNotes())) project.setNotes(List.of(note));
        else project.getNotes().add(note);

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
        Note note = findById(noteId);

        try {
            File file = new File(multipartFile);
            fileService.save(file);
            note.setFiles(List.of(file));
            System.out.println(note);
        } catch (Exception ignored) {
            throw new RuntimeException();
        }

        return noteRepository.save(note);
    }

    public List<NoteDTO> findAllByProject(Long projectId) {
        List<NoteDTO> list = noteRepository
                .findAllByProject_Id(projectId)
                .stream()
                .map(NoteDTO::new)
                .toList();
        System.out.println(list);
        return list;
    }

    public void delete(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
        }
        throw new EntityNotFoundException();
    }

}
