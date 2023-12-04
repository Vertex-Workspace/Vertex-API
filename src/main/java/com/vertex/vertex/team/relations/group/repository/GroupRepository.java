package com.vertex.vertex.team.relations.group.repository;

import com.vertex.vertex.team.relations.group.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByTeam_IdAndId(Long teamId, Long groupId);
}
