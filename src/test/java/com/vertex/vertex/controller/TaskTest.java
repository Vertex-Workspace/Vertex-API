//package com.vertex.vertex.controller;
//
//import com.vertex.vertex.project.model.entity.Project;
//import com.vertex.vertex.project.service.ProjectService;
//import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
//import com.vertex.vertex.task.model.entity.Task;
//import com.vertex.vertex.task.repository.TaskRepository;
//import com.vertex.vertex.task.service.TaskService;
//import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
//import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
//import com.vertex.vertex.user.model.entity.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class TaskTest {
//
//    @Autowired
//    private TaskService taskService;
//
//    @Autowired
//    private ProjectService projectService;
//
//    @Autowired
//    private UserTeamService userTeamService;
//
//    private TaskCreateDTO taskCreateDTO;
//    private Project project;
//    private User creator;
//
//    @BeforeEach
//    public void setUp() {
//        project = new Project();
//        project.setName("Test Project");
//        project = projectService.save(project);
//
//        creator = new User();
//        creator.setFirstName("testuser");
//        creator.setEmail("testuser@example.com");
//
//        UserTeam userTeam = new UserTeam();
//        userTeam.setUser(creator);
//        userTeam.setTeam(project.getTeam());
//        userTeamService.save(userTeam);
//
//        taskCreateDTO = new TaskCreateDTO();
//        taskCreateDTO.setName("Test Task");
//        taskCreateDTO.setDescription("This is a test task");
//        taskCreateDTO.setCreator(creator);
//        taskCreateDTO.setProject(project);
//    }
//
//    @Test
//    public void testSaveTask() {
//        Task savedTask = taskService.save(taskCreateDTO);
//
//        assertNotNull(savedTask);
//        assertNotNull(savedTask.getId());
//        assertEquals(taskCreateDTO.getName(), savedTask.getName());
//        assertEquals(taskCreateDTO.getDescription(), savedTask.getDescription());
//        assertEquals(taskCreateDTO.getCreator().getFirstName(), savedTask.getCreator().getUser().getFirstName());
//        assertEquals(taskCreateDTO.getProject().getId(), savedTask.getProject().getId());
//
//        assertNotNull(savedTask.getValues());
//        assertFalse(savedTask.getValues().isEmpty());
//
//        assertNotNull(savedTask.getTaskResponsables());
//        assertFalse(savedTask.getTaskResponsables().isEmpty());
//
//        assertTrue(savedTask.isRevisable());
//    }
//}
