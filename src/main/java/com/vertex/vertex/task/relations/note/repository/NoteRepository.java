package com.vertex.vertex.task.relations.note.repository;

import com.vertex.vertex.task.relations.note.model.entity.Note;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository
        extends JpaRepository<Note, Long> {

    List<Note> findAllByProject_Id(Long id);

}
