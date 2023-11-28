package com.vertex.vertex.task.value.repository;

import com.vertex.vertex.task.value.model.entity.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueRepository extends JpaRepository<Value,Long> {
}
