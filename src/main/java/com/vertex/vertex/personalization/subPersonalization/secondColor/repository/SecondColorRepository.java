package com.vertex.vertex.personalization.subPersonalization.secondColor.repository;

import com.vertex.vertex.personalization.subPersonalization.secondColor.model.entity.SecondColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondColorRepository extends JpaRepository<SecondColor,Long> {
}
