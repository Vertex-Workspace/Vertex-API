package com.vertex.vertex.user.relations.personalization.relations.primaryColor.repository;

import com.vertex.vertex.user.relations.personalization.relations.primaryColor.model.entity.PrimaryColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimaryColorRepository extends JpaRepository<PrimaryColor,Long> {
}
