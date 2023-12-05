package com.vertex.vertex.user.relations.personalization.relations.secondColor.repository;

import com.vertex.vertex.user.relations.personalization.relations.secondColor.model.entity.SecondColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondColorRepository extends JpaRepository<SecondColor,Long> {
}
