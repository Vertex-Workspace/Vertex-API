package com.vertex.vertex.personalization.subPersonalization.primaryColor.repository;

import com.vertex.vertex.personalization.subPersonalization.primaryColor.model.entity.PrimaryColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimaryColorRepository extends JpaRepository<PrimaryColor,Long> {
}
