package com.vertex.vertex.task.relations.note.controller;

import com.vertex.vertex.task.relations.note.model.dto.NoteDTO;
import com.vertex.vertex.task.relations.note.model.dto.NoteEditDTO;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import com.vertex.vertex.task.relations.note.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
@AllArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/{projectId}")
    public ResponseEntity<List<Note>> findAllByProject(
            Long projectId) {
        return new ResponseEntity<>
                (noteService.findAllByProject(projectId),
                        HttpStatus.OK);
    }

    @PostMapping("/{projectId}/{userId}")
    public ResponseEntity<?> create(
            @RequestBody NoteDTO dto,
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        try {
            return new ResponseEntity<>
                    (noteService.create(dto, projectId, userId),
                            HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @PatchMapping
    public ResponseEntity<?> editPatch(
            @RequestBody NoteEditDTO dto) {
        try {
            return new ResponseEntity<>
                    (noteService.editPatch(dto),
                            HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {
        try {
            noteService.delete(id);
            return new ResponseEntity<>
                    (HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

}
