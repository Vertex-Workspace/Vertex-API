package com.vertex.vertex.user.relations.personalization.relations.fontSize.repository;

import com.vertex.vertex.user.relations.personalization.relations.fontSize.model.entity.FontSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FontSizeRepository extends JpaRepository<FontSize,Long> {
}
