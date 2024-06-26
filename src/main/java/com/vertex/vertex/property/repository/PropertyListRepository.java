package com.vertex.vertex.property.repository;

import com.vertex.vertex.property.model.entity.PropertyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyListRepository extends JpaRepository<PropertyList, Long> {
}
