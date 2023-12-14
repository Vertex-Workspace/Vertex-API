package com.vertex.vertex.project.repository;

import com.vertex.vertex.project.model.entity.Project;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Set<Project> findAllByTeam_Id(Long id);
}
