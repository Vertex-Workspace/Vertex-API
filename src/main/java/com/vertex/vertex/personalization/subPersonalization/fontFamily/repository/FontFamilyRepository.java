package com.vertex.vertex.personalization.subPersonalization.fontFamily.repository;

import com.vertex.vertex.personalization.subPersonalization.fontFamily.model.entity.FontFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FontFamilyRepository extends JpaRepository<FontFamily,Long> {
}
