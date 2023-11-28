package com.vertex.vertex.property.repository;


import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property_list.model.entity.PropertyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {


}
