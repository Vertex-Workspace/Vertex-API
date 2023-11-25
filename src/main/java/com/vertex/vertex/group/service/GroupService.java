package com.vertex.vertex.group.service;

import com.vertex.vertex.comment.model.Comment;
import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.group.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    public Group save(Group group){
        return groupRepository.save(group);
    }

    public List<Group> findAll(){
        return groupRepository.findAll();
    }

    public Group findById(Long id){
        return groupRepository.findById(id).get();
    }

    public void deleteById(Long id){
        groupRepository.deleteById(id);
    }
}
