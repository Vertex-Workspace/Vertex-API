package com.vertex.vertex.personalization.repository;

import com.vertex.vertex.personalization.model.entity.Personalization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalizationRepository extends JpaRepository<Personalization, Long> {
}
