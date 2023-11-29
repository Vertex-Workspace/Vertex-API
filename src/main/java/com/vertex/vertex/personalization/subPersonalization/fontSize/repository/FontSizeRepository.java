package com.vertex.vertex.personalization.subPersonalization.fontSize.repository;

import com.vertex.vertex.personalization.subPersonalization.fontSize.model.entity.FontSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FontSizeRepository extends JpaRepository<FontSize,Long>{
}
