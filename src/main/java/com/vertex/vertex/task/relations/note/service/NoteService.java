package com.vertex.vertex.task.relations.note.service;

import com.vertex.vertex.file.File;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.task.relations.note.model.dto.NoteDTO;
import com.vertex.vertex.task.relations.note.model.dto.NoteEditDTO;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import com.vertex.vertex.task.relations.note.repository.NoteRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import com.vertex.vertex.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@AllArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserTeamService userTeamService;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    public Note create(NoteDTO dto, Long projectId, Long userId) {
        Project project = projectService.findById(projectId);
        UserTeam creator = userTeamService
                .findUserTeamByComposeId
                        (userId, project.getTeam().getId());

        Note note = new Note();
        note.setProject(project);
        note.setCreator(creator);
        BeanUtils.copyProperties(dto, note);
        return noteRepository.save(note);
    }

    public Note findById(Long id) {
        Optional<Note> note = noteRepository.findById(id);

        if (note.isPresent()) {
            return note.get();
        }

        throw new EntityNotFoundException();
    }

    public Note editPatch(NoteEditDTO dto) {
        Note note = findById(dto.getId());
        modelMapper.map(dto, note);
        return noteRepository.save(note);
    }

    public Note uploadImage(Long noteId, MultipartFile multipartFile) {
        Note note = findById(noteId);
        try {
            File file = new File(multipartFile);
            note.getFiles().add(file);

        } catch (Exception ignored) {
            throw new RuntimeException();
        }

        return noteRepository.save(note);
    }

}
