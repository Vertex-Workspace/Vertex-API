package com.vertex.vertex.property_list.repository;

import com.vertex.vertex.personalization.model.entity.Personalization;
import com.vertex.vertex.property_list.model.entity.PropertyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyListRepository extends JpaRepository<PropertyList, Long> {
}
