package com.vertex.vertex.task.relations.review.repository;

import com.vertex.vertex.task.relations.review.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
