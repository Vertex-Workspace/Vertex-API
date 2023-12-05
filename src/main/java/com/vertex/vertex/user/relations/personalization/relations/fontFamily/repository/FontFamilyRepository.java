package com.vertex.vertex.user.relations.personalization.relations.fontFamily.repository;

import com.vertex.vertex.user.relations.personalization.relations.fontFamily.model.entity.FontFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FontFamilyRepository extends JpaRepository<FontFamily,Long> {
}
