package com.vertex.vertex.task.relations.comment.repository;

import com.vertex.vertex.task.relations.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
