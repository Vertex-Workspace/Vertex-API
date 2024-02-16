package com.vertex.vertex.user.relations.personalization.repository;

import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalizationRepository extends JpaRepository<Personalization, Long> {

    Personalization findByUser_Id(Long id);

}
