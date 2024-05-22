package com.vertex.vertex.config;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.ENUM.Color;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.ENUM.PropertyStatus;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.task_responsables.repository.TaskResponsablesRepository;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.model.enums.UserKind;
import com.vertex.vertex.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class DefaultConfigService {

    private final TeamService teamService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final TaskResponsablesRepository taskResponsablesRepository;
    private static final String BANCO_URL = "jdbc:mysql://localhost:3306/vertex";
    private static final String USERNAME = "root";
    private static final String PASSSWORD = "root";

    public void createTeam(User user){
        TeamViewListDTO teamViewListDTO =
                new TeamViewListDTO("Equipe " + user.getFirstName(), user, null,
                        "“As boas equipes incorporam o trabalho em equipe na sua cultura, criando os elementos essenciais ao sucesso.” — Ted Sundquist, jogador de futebol americano.", LocalDateTime.now(), true);
        Team team = teamService.save(teamViewListDTO);
        createProjectAndCollaboratorsDefault(team);
    }

    public void createProjectAndCollaboratorsDefault(Team team) {
        try (Connection connection = DriverManager.getConnection(BANCO_URL, USERNAME, PASSSWORD)) {
            // Inserir os projetos
            String insertProjectSQL = "INSERT INTO project (name, description, project_reviewenum, creator_id, team_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertProjectSQL)) {
                // Projeto 1
                preparedStatement.setString(1, "Projeto pessoal");
                preparedStatement.setString(2, "Nesse projeto você pode organizar sua rotina de forma intuitiva");
                preparedStatement.setString(3, "EMPTY");
                preparedStatement.setLong(4, team.getCreator().getId());
                preparedStatement.setLong(5, team.getId());
                preparedStatement.executeUpdate();
            }

                for(Project project : projectService.findAllByTeam(team.getId())){

                    project = defaultPropertyList(project);
                    project.getCollaborators().add(team.getCreator());

                    if(team.getCreator().getUser().getUserKind().equals(UserKind.GOOGLE)){
                        System.out.println("google");
                        // Inserir os projetos
                        String insertProjectSQL2 = "INSERT INTO `project_collaborators` (`project_id`, `collaborators_id`) " +
                                "VALUES (?, ?)";

                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProjectSQL2)) {
                            // Projeto 1
                            preparedStatement.setLong(1, project.getId());
                            preparedStatement.setLong(2, project.getCreator().getId());
                            preparedStatement.executeUpdate();
                        }
                    }
                    createTasks(project.getCreator().getUser(), project);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }

    public List<PropertyList> defaultStatus(Property property) {
        List<PropertyList> propertiesList = new ArrayList<>();
        propertiesList.add(new PropertyList("Não Iniciado", Color.RED, property, PropertyListKind.TODO, true));
        propertiesList.add(new PropertyList("Em Andamento", Color.YELLOW, property, PropertyListKind.DOING, true));
        propertiesList.add(new PropertyList("Pausado", Color.BLUE, property, PropertyListKind.DOING, false));
        propertiesList.add(new PropertyList("Concluído", Color.GREEN, property, PropertyListKind.DONE, true));
        return propertiesList;
    }

    public List<Property> defaultProperties() {
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(PropertyKind.STATUS, "Status", true, null, PropertyStatus.FIXED));
        properties.add(new Property(PropertyKind.DATE, "Data", true, null, PropertyStatus.FIXED));
        properties.add(new Property(PropertyKind.LIST, "Dificuldade", false, null, PropertyStatus.VISIBLE));
        properties.add(new Property(PropertyKind.NUMBER, "Número", false, null, PropertyStatus.VISIBLE));
        properties.add(new Property(PropertyKind.TEXT, "Palavra-Chave", false, null, PropertyStatus.INVISIBLE));
        return properties;
    }

    public Project defaultPropertyList(Project project) {
        for (Property property : defaultProperties()) {
            if (property.getKind() == PropertyKind.STATUS) {
                property.setPropertyLists(defaultStatus(property));
            }
            if (property.getKind() == PropertyKind.LIST) {
                List<PropertyList> propertiesList = new ArrayList<>();
                propertiesList.add(new PropertyList("Fácil", Color.GREEN, property, PropertyListKind.VISIBLE, false));
                propertiesList.add(new PropertyList("Médio", Color.YELLOW, property, PropertyListKind.VISIBLE, true));
                propertiesList.add(new PropertyList("Díficil", Color.RED, property, PropertyListKind.VISIBLE, true));
                propertiesList.add(new PropertyList("Não validado", Color.BLUE, property, PropertyListKind.INVISIBLE, true));
                property.setPropertyLists(propertiesList);
            }
            property.setProject(project);
            project.addProperty(property);
        }
        return projectService.save(project);
    }

    //solve taskResponsables issue
    public void createTasks(User user, Project project) {
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO("Estudar", "Sua tarefa é estudar",
                user, project);
       TaskCreateDTO taskCreateDTO2 = new TaskCreateDTO("Ler um livro", "Sua tarefa é ler um livro",
               user, project);
       TaskCreateDTO taskCreateDTO3 = new TaskCreateDTO("Viajar", "Sua tarefa é viajar",
               user, project);

        taskService.savePostConstruct(taskCreateDTO);
        taskService.savePostConstruct(taskCreateDTO2);
        taskService.savePostConstruct(taskCreateDTO3);
    }
}
