package com.vertex.vertex.config;

import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.task_responsables.repository.TaskResponsablesRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.value.model.entity.ValueFile;
import com.vertex.vertex.task.relations.value.model.entity.ValueList;
import com.vertex.vertex.task.relations.value.repository.ValueRepository;
import com.vertex.vertex.task.relations.value.service.ValueService;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import com.vertex.vertex.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Configuration;

import java.sql.*;

@Configuration
@AllArgsConstructor
public class DataConfig {

    private UserService userService;
    private TeamService teamService;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private UserTeamRepository userTeamRepository;
    private ValueService valueService;
    private TaskService taskService;
    private TaskResponsablesRepository taskResponsablesRepository;
    private static final String BANCO_URL = "jdbc:mysql://localhost:3306/vertex";
    private static final String USERNAME = "root";
    private static final String PASSSWORD = "root";
    private static String comandoSql;


    @PostConstruct
    public void init() {

        //default Users
            userService.save(new UserDTO
                    ("ana@gmail.com", "We3sl@ey", "Ana", "Borchardt"));
            userService.save(new UserDTO
                    ("kaique@gmail.com", "We3sl@ey", "Kaique", "Fernandes"));
            User user3 = userService.save(new UserDTO
                    ("otavio@gmail.com", "We3sl@ey", "Otavio", "Miguel Rocha"));
            userService.save(new UserDTO
                    ("miguel@gmail.com", "We3sl@ey", "Miguel", "Bertoldi"));
//
            teamService.save(new TeamViewListDTO
                    ("Vertex", user3, "A Equipe vertex tem como propósito organizar as tarefas e funcionalidades ...", false));

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

            // Inserir usuário "Ana" na equipe "Vertex"
            statement.executeUpdate("INSERT INTO `user_team` (`team_id`, `user_id`)" +
                    " SELECT t.id, u.id" +
                    " FROM `team` t" +
                    " CROSS JOIN `user` u" +
                    " WHERE t.name = 'Vertex' AND u.email = 'ana@gmail.com'");

            // Inserir usuário "Miguel" na equipe "Vertex"
            statement.executeUpdate("INSERT INTO `user_team` (`team_id`, `user_id`)" +
                    " SELECT t.id, u.id" +
                    " FROM `team` t" +
                    " CROSS JOIN `user` u" +
                    " WHERE t.name = 'Vertex' AND u.email = 'miguel@gmail.com'");

            // Inserir usuário "Kaique" na equipe "Vertex"
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
                        " WHERE t.name = 'Vertex'";
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
                    "(false, 'Criar', 6), (false, 'Editar', 6), (false, 'Deletar', 6), " +
                    "(false, 'Criar', 7), (false, 'Editar', 7), (false, 'Deletar', 7), " +
                    "(false, 'Criar', 8), (false, 'Editar', 8), (false, 'Deletar', 8)";
            statement.executeUpdate(sql);

            System.out.println("Permissões criadas com sucesso.");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void createProjectAndCollaborators() {
        try (Connection connection = DriverManager.getConnection(BANCO_URL, USERNAME, PASSSWORD)) {
            // Inserir os projetos
            String insertProjectSQL = "INSERT INTO project (description, name, project_reviewenum, creator_id, team_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertProjectSQL)) {
                // Projeto 1
                preparedStatement.setString(1, "Ideias e Planejamento");
                preparedStatement.setString(2, "Ideias e Planejamento");
                preparedStatement.setString(3, "MANDATORY");
                preparedStatement.setLong(4, 5);
                preparedStatement.setLong(5, 5);
                preparedStatement.executeUpdate();

                // Projeto 2
                preparedStatement.setString(1, "Descrição do Projeto 2");
                preparedStatement.setString(2, "Projeto 2");
                preparedStatement.setString(3, "OPTIONAL");
                preparedStatement.setLong(4, 5);
                preparedStatement.setLong(5, 5);
                preparedStatement.executeUpdate();

                // Projeto 3
                preparedStatement.setString(1, "Descrição do Projeto 3");
                preparedStatement.setString(2, "Projeto 3");
                preparedStatement.setString(3, "EMPTY");
                preparedStatement.setLong(4, 5);
                preparedStatement.setLong(5, 5);
                preparedStatement.executeUpdate();
            }

            // Adicionar membros da equipe como colaboradores
            String insertCollaboratorsSQL = "INSERT INTO project_collaborators (project_id, collaborators_id) " +
                    "SELECT p.id, ut.id FROM project p, user_team ut WHERE p.team_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertCollaboratorsSQL)) {
                preparedStatement.setLong(1, 5);
                preparedStatement.executeUpdate();
            }

            System.out.println("Inserções realizadas com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createProperties() {
        try {
            // Create a connection
            Connection connection = DriverManager.getConnection(BANCO_URL, USERNAME, PASSSWORD);

            // Insert statements
            String insertPropertyQuery = "INSERT INTO property (is_obligate, kind, name, property_status, project_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
            String insertPropertyListQuery = "INSERT INTO property_list (color, is_fixed, property_list_kind, value, property_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

            // Prepare statements
            PreparedStatement insertPropertyStatement = connection.prepareStatement(insertPropertyQuery);
            PreparedStatement insertPropertyListStatement = connection.prepareStatement(insertPropertyListQuery);

            // Execute insertions
            // Insert into property table
            for (int i = 1; i <= 3; i++) {
                insertPropertyStatement.setInt(1, 1);
                insertPropertyStatement.setString(2, "STATUS");
                insertPropertyStatement.setString(3, "Status");
                insertPropertyStatement.setString(4, "FIXED");
                insertPropertyStatement.setInt(5, i);
                insertPropertyStatement.executeUpdate();

                // Insert into property_list table
                insertPropertyListStatement.setString(1, "#ffe2dd");
                insertPropertyListStatement.setInt(2, 1);
                insertPropertyListStatement.setString(3, "DONE");
                insertPropertyListStatement.setString(4, "Concluído");
                insertPropertyListStatement.setInt(5, i);
                insertPropertyListStatement.executeUpdate();

                insertPropertyListStatement.setString(1, "#fdecc8");
                insertPropertyListStatement.setInt(2, 1);
                insertPropertyListStatement.setString(3, "DOING");
                insertPropertyListStatement.setString(4, "Em Andamento");
                insertPropertyListStatement.setInt(5, i);
                insertPropertyListStatement.executeUpdate();

                insertPropertyListStatement.setString(1, "#d3e5ef");
                insertPropertyListStatement.setInt(2, 0);
                insertPropertyListStatement.setString(3, "DOING");
                insertPropertyListStatement.setString(4, "Pausado");
                insertPropertyListStatement.setInt(5, i);
                insertPropertyListStatement.executeUpdate();

                insertPropertyListStatement.setString(1, "#dbeddb");
                insertPropertyListStatement.setInt(2, 1);
                insertPropertyListStatement.setString(3, "TODO");
                insertPropertyListStatement.setString(4, "Não Iniciado");
                insertPropertyListStatement.setInt(5, i);
                insertPropertyListStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTasks() {
//        TaskCreateDTO taskCreateDTO = new TaskCreateDTO("Regras de Negócio", "Escreva todas as regras de negócio aqui",
//                userRepository.findById(3L).get(), projectRepository.findById(1L).get());
//        TaskCreateDTO taskCreateDTO2 = new TaskCreateDTO("Criar Models", "Separar em entities e dtos",
//                userRepository.findById(1L).get(), projectRepository.findById(1L).get());
//        TaskCreateDTO taskCreateDTO3 = new TaskCreateDTO("Fazer requisições", "criar services e fazer as requisições",
//                userRepository.findById(1L).get(), projectRepository.findById(1L).get());
//
//        taskService.savePostConstruct(taskCreateDTO);
//        taskService.savePostConstruct(taskCreateDTO2);
//        taskService.savePostConstruct(taskCreateDTO3);
//



    }

}
