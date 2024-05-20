package com.vertex.vertex.config;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.ENUM.Color;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.ENUM.PropertyStatus;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.relations.task_responsables.repository.TaskResponsablesRepository;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import com.vertex.vertex.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class DataConfig {

    private UserService userService;
    private TeamService teamService;
    private TeamRepository teamRepository;
    private ProjectService projectService;
    private UserRepository userRepository;
    private TaskService taskService;
    private ProjectRepository projectRepository;
    private static final String BANCO_URL = "jdbc:mysql://localhost:3306/vertex";
    private static final String USERNAME = "root";
    private static final String PASSSWORD = "root";


    @PostConstruct
    public void init() throws IOException {
        if(!userService.findAll().isEmpty()){
            return;
        }
        //default Users
        User user1 = userService.save(new UserDTO
                ("kaique@gmail.com", "@Weg123456", "Kaique", "Fernandes"));
        User user2 = userService.save(new UserDTO
                ("otavio@gmail.com", "@Weg123456", "Otávio", "Miguel Rocha"));
        User user3 = userService.save(new UserDTO
                ("miguel@gmail.com", "@Weg123456", "Miguel", "Bertoldi"));
        setUserImage(user1, user2, user3);
        teamRepository.deleteById(1L);
        teamRepository.deleteById(2L);
        teamRepository.deleteById(3L);
        teamService.save(new TeamViewListDTO
                ("Vertex", user2, setTeamImage(), "A Equipe vertex tem como propósito organizar as tarefas e funcionalidades ...", false));

        sendInformation();
        associateUserTeamsWithChat();
        createPermissions();
        createProjectAndCollaborators();
        createProperties();
        createTasks();
    }

    public void sendInformation() {
        try (Connection conn = DriverManager.getConnection(BANCO_URL, USERNAME, PASSSWORD);
             Statement statement = conn.createStatement()) {

            statement.executeUpdate("INSERT INTO `user_team` (`team_id`, `user_id`)" +
                    " SELECT t.id, u.id" +
                    " FROM `team` t" +
                    " CROSS JOIN `user` u" +
                    " WHERE t.name = 'Vertex' AND u.email = 'miguel@gmail.com'");

            statement.executeUpdate("INSERT INTO `user_team` (`team_id`, `user_id`)" +
                    " SELECT t.id, u.id" +
                    " FROM `team` t" +
                    " CROSS JOIN `user` u" +
                    " WHERE t.name = 'Vertex' AND u.email = 'kaique@gmail.com'");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void associateUserTeamsWithChat() {
        try (Connection conn = DriverManager.getConnection(BANCO_URL, USERNAME, PASSSWORD);
             Statement statement = conn.createStatement()) {

            // Selecionar o ID do chat de nome "Vertex"
            ResultSet chatResult = statement.executeQuery("SELECT id FROM `chat` WHERE name = 'Vertex'");
            if (chatResult.next()) {
                long chatId = chatResult.getLong("id");

                // Associar os UserTeams da equipe "Vertex" ao chat "Vertex"
                String sql = "INSERT INTO `chat_user_teams` (`chats_id`, `user_teams_id`)" +
                        " SELECT " + chatId + ", ut.id" +
                        " FROM `user_team` ut" +
                        " JOIN `team` t ON ut.team_id = t.id" +
                        " WHERE t.name = 'Vertex' AND ut.user_id <> '2'";
                statement.executeUpdate(sql);

                System.out.println("Associações entre equipes de usuário e chat foram criadas com sucesso.");
            } else {
                System.out.println("O chat 'Vertex' não foi encontrado no banco de dados.");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void createPermissions() {
        try (Connection conn = DriverManager.getConnection(BANCO_URL, USERNAME, PASSSWORD);
             Statement statement = conn.createStatement()) {

            // Criar as permissões para os UserTeams com IDs 6, 7 e 8
            String sql = "INSERT INTO `permission` (`enabled`, `name`, `user_team_id`) VALUES " +
                    "(false, 'Criar', 5), (false, 'Editar', 5), (false, 'Deletar', 5), " +
                    "(false, 'Criar', 6), (false, 'Editar', 6), (false, 'Deletar', 6); ";
            statement.executeUpdate(sql);

            System.out.println("Permissões criadas com sucesso.");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createProjectAndCollaborators() {
        try (Connection connection = DriverManager.getConnection(BANCO_URL, USERNAME, PASSSWORD)) {
            // Inserir os projetos
            String insertProjectSQL = "INSERT INTO project (description, name, project_reviewenum, creator_id, team_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertProjectSQL)) {
                // Projeto 1
                preparedStatement.setString(1, "Ideias e Planejamento");
                preparedStatement.setString(2, "Ideias e Planejamento");
                preparedStatement.setString(3, "MANDATORY");
                preparedStatement.setLong(4, 4);
                preparedStatement.setLong(5, 4);
                preparedStatement.executeUpdate();

                // Projeto 2
                preparedStatement.setString(1, "Desenvolvimento backend");
                preparedStatement.setString(2, "Desenvolvimento backend");
                preparedStatement.setString(3, "MANDATORY");
                preparedStatement.setLong(4, 4);
                preparedStatement.setLong(5, 4);
                preparedStatement.executeUpdate();

                // Projeto 3
                preparedStatement.setString(1, "Desenvolvimento frontend");
                preparedStatement.setString(2, "Desenvolvimento frontend");
                preparedStatement.setString(3, "MANDATORY");
                preparedStatement.setLong(4, 4);
                preparedStatement.setLong(5, 4);
                preparedStatement.executeUpdate();
            }

            // Adicionar membros da equipe como colaboradores
            try (Connection conn = DriverManager.getConnection(BANCO_URL, USERNAME, PASSSWORD);
                 Statement statement = conn.createStatement()) {

                // Criar as permissões para os UserTeams com IDs 6, 7 e 8
                String sql = "INSERT INTO `project_collaborators` (`project_id`, `collaborators_id`) VALUES " +
                        "(4, 4), (4,5), (4,6), (5, 4), (5,5), (5,6), (6, 4), (6,5), (6,6)";
                statement.executeUpdate(sql);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            setProjectImage();
            System.out.println("Inserções realizadas com sucesso!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void createProperties() {
        for (long i = 4; i <=6 ; i++) {
            Project project = projectService.findById(i);
            defaultPropertyList(project);
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

    public void defaultPropertyList(Project project) {
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
        projectService.save(project);
    }

    public void createTasks() {
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO("Regras de Negócio", "Escreva todas as regras de negócio aqui",
                userRepository.findById(2L).get(), projectRepository.findById(4L).get());
        TaskCreateDTO taskCreateDTO2 = new TaskCreateDTO("Criar Models", "Separar em entities e dtos",
                userRepository.findById(2L).get(), projectRepository.findById(5L).get());
        TaskCreateDTO taskCreateDTO3 = new TaskCreateDTO("Fazer requisições", "criar services e fazer as requisições",
                userRepository.findById(2L).get(), projectRepository.findById(6L).get());

        taskService.savePostConstruct(taskCreateDTO);
        taskService.savePostConstruct(taskCreateDTO2);
        taskService.savePostConstruct(taskCreateDTO3);
    }

    public byte[] setTeamImage() throws IOException {
        try {
            String imagePath = "src/main/java/com/vertex/vertex/upload/vertex logo2.png";

            return Files.readAllBytes(Paths.get(imagePath));
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void setProjectImage() throws IOException {
        try {
            String imagePath1 = "src/main/java/com/vertex/vertex/upload/undraw_Mind_map_re_nlb6.png";
            String imagePath2 = "src/main/java/com/vertex/vertex/upload/undraw_File_sync_re_0pcx.png";
            String imagePath3 = "src/main/java/com/vertex/vertex/upload/undraw_Code_review_re_woeb.png";

            Project project1 = projectRepository.findById(4L).get();
            project1.setFile(new File(Files.readAllBytes(Paths.get(imagePath1))));
            projectRepository.save(project1);

            Project project2 = projectRepository.findById(5L).get();
            project2.setFile(new File(Files.readAllBytes(Paths.get(imagePath2))));
            projectRepository.save(project2);

            Project project3 = projectRepository.findById(6L).get();
            project3.setFile(new File(Files.readAllBytes(Paths.get(imagePath3))));
            projectRepository.save(project3);

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void setUserImage(User user1, User user2, User user3) throws IOException {
        try {
            user1.setImage(Files.readAllBytes(Paths.get("src/main/java/com/vertex/vertex/upload/kaique.jpg")));
            userRepository.save(user1);

            user2.setImage(Files.readAllBytes(Paths.get("src/main/java/com/vertex/vertex/upload/otavio.jpg")));
            userRepository.save(user2);

            user3.setImage(Files.readAllBytes(Paths.get("src/main/java/com/vertex/vertex/upload/kaique.jpg")));
            userRepository.save(user3);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}