package com.vertex.vertex.comment.service;

import com.vertex.vertex.comment.model.Comment;
import com.vertex.vertex.comment.repository.CommentRepository;
import com.vertex.vertex.project.model.DTO.ProjectDTO;
import com.vertex.vertex.project.model.DTO.ProjectEditionDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }

    public List<Comment> findAll(){
        return commentRepository.findAll();
    }

    public Comment findById(Long id){
        return commentRepository.findById(id).get();
    }

    public void deleteById(Long id){
        commentRepository.deleteById(id);
    }
}
