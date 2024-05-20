package com.vertex.vertex.user.service;

import com.vertex.vertex.config.DefaultConfigService;
import com.vertex.vertex.log.model.exception.EntityDoesntExistException;
import com.vertex.vertex.notification.service.NotificationService;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.security.authentication.UserDetailsServiceImpl;
import com.vertex.vertex.task.model.DTO.TaskSearchDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_hours.service.TaskHoursService;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.service.GroupService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.DTO.*;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.model.exception.*;
import com.vertex.vertex.user.relations.personalization.model.entity.LanguageDTO;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import com.vertex.vertex.user.relations.personalization.service.PersonalizationService;
import com.vertex.vertex.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.*;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PersonalizationService personalizationService;
    private final GroupService groupService;
    private final NotificationService notificationService;
    private final ModelMapper mapper;
    private final TeamService teamService;
    private final RegexValidate regexValidate;
    private final TaskHoursService taskHoursService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final SecurityContextRepository securityContextRepository;
    private final UserTeamService userTeamService;
    private final DefaultConfigService defaultConfig;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User save(UserDTO userDTO) {
        try {
            User user = new User(userDTO);

            if (existsByEmail(user.getEmail())) {
                throw new EmailAlreadyExistsException();
            }

            if (regexValidate.isEmailSecure(user.getEmail())) {
                user.setEmail(user.getEmail());
            } else {
                throw new InvalidEmailException();
            }
            //if(!userDTO.isDefaultUser()){
            //    regexValidate.isPasswordSecure(user, userDTO);
            //}
            userRepository.save(user);
            userSetDefaultInformations(user);
            user.setDefaultSettings(false);

            if(userDTO.getImage() != null){
                try {
                    byte[] data = Base64.getDecoder().decode(userDTO.getImage());
                    user.setImage(data); // default user
                } catch (Exception ignored) {
                    userDTO.setImage(userDTO.getImage().replace("s96", "s800")); //upscale image
                    user.setImgUrl(userDTO.getImage()); // external
                }
            }

            return save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void userSetDefaultInformations(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setLocation("Brasil");
        user.setPersonalization(personalizationService.defaultSave(user));
        user.setTaskReview(true);
        user.setNewMembersAndGroups(true);
        user.setPermissionsChanged(true);
        user.setSendToEmail(true);
        user.setAnyUpdateOnTask(true);
        user.setResponsibleInProjectOrTask(true);
        user.setDefaultSettings(true);
        defaultConfig.createTeam(user);
    }

    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        throw new InvalidEmailException();
    }


    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private void emailValidation(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        boolean validEmail = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
                .matcher(user.getEmail())
                .find();

        if (!validEmail) {
            throw new InvalidEmailException();
        }
    }

    public User edit(UserEditionDTO userEditionDTO) throws Exception {
        User user = findById(userEditionDTO.getId());
        if(!Objects.equals(user.getEmail(), userEditionDTO.getEmail())){
            emailValidation(user);
        }
        user.setEmail(userEditionDTO.getEmail());
        user.setDescription(userEditionDTO.getDescription());
        user.setLocation(userEditionDTO.getLocation());
        user.setFirstName(userEditionDTO.getFirstName());
        user.setLastName(userEditionDTO.getLastName());
        return userRepository.save(user);
    }

    public User patchUserFirstAccess(UserLoginDTO userLoginDTO) {
        User user = findByEmail(userLoginDTO.getEmail());
        user.setFirstAccess(false);
        return userRepository.save(user);
    }

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(EntityDoesntExistException::new);
    }

    public UserPublicProfileDTO findUserInformations(Long id) {
        User user = findById(id);
        UserPublicProfileDTO dto = new UserPublicProfileDTO();
        mapper.map(user, dto);
        dto.setFullname(user.getFullName());
        User userLogged = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Team> teams = userTeamService.findTeamsByUserId(user.getId());
        if(teams.stream().flatMap(team -> team.getUserTeams().stream()).map(UserTeam::getUser).noneMatch(u -> u.getId().equals(userLogged.getId()))){
            throw new AuthenticationCredentialsNotFoundException("Usuário não compartilha nenhuma equipe com você!");
        }
        //All tasks that the user is responsible
        List<TaskResponsable> tasksResponsible =
                teams.stream().flatMap(team -> team.getUserTeams().stream())
                        .flatMap(userTeam -> userTeam.getTeam().getProjects().stream())
                        .flatMap(project -> project.getTasks().stream())
                        .flatMap(task -> task.getTaskResponsables().stream())
                        .filter(taskResponsable -> taskResponsable.getUserTeam().getUser().getId().equals(id))
                        .toList();

        List<PropertyListKind> listKinds = List.of(PropertyListKind.TODO, PropertyListKind.DOING, PropertyListKind.DONE);
        for (PropertyListKind propertyListKind : listKinds) {
            int sumFinal = 0;
            for (TaskResponsable taskResponsable : tasksResponsible) {
                sumFinal += taskResponsable.getTask().getValues()
                        .stream()
                        .filter(value -> value.getProperty().getKind().equals(PropertyKind.STATUS)
                                && ((PropertyList) value.getValue()).getPropertyListKind().equals(propertyListKind))
                        .toList().size();
            }
            dto.getTasksPerformances().add(sumFinal);
        }

        Duration duration = Duration.ZERO;
        for (TaskResponsable taskResponsable : tasksResponsible) {
            duration = duration.plus(taskHoursService.calculateTimeOnTask(taskResponsable));
        }
        dto.setTime(LocalTime.MIDNIGHT.plus(duration));


        //Refatorar obrigatoriamente - ass. Otávio
        List<Task> tasks = tasksResponsible.stream().map(TaskResponsable::getTask).toList();
        List<TaskResponsable> taskResponsables = tasks.stream()
                .flatMap(task -> task.getTaskResponsables().stream())
                .filter(taskResponsable -> taskResponsable.getUserTeam().getUser().getId().equals(userLogged.getId()))
                .toList();
        dto.setTasksInCommon(taskResponsables.stream()
                .map(TaskResponsable::getTask)
                .map(TaskSearchDTO::new).toList());

        return dto;
    }


    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }


    public void saveImage(MultipartFile imageFile, Long id) {
        try {
            User user = findById(id);
            user.setImage(imageFile.getBytes());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User patchUserPersonalization(Long id, Personalization personalization) {
        User user = findById(id);
        personalization.setId(user.getPersonalization().getId());
        personalization.setUser(user);
        user.setPersonalization(personalization);
        return userRepository.save(user);
    }

    public User patchUserShowCharts(Long id) {
        User user = findById(id);
        if(user.getShowCharts() == null){
            user.setShowCharts(true);
        } else {
            user.setShowCharts(!user.getShowCharts());
        }
        return userRepository.save(user);
    }


    public User changeLanguage(LanguageDTO languageDTO, Long userId){
        User user = findById(userId);

        Personalization personalization = personalizationService.findById(user.getPersonalization().getId());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(languageDTO,personalization);

        user.setPersonalization(personalization);
        return userRepository.save(user);
    }

    public User patchUserPassword(UserLoginDTO userLoginDTO) {
        User user = findByEmail(userLoginDTO.getEmail());
        user.setPassword(userLoginDTO.getPassword());
        return userRepository.save(user);
    }

    public List<User> getUsersByGroup(Long groupId) {
        List<User> users = new ArrayList<>();
        Group group = groupService.findById(groupId);

        for (int i = 0; i < group.getUserTeams().size(); i++) {
            users.add(group.getUserTeams().get(i).getUser());
        }
        return users;
    }


    public List<UserSearchDTO> findAllByUserAndQuery(Long userId, String query) {
        return teamService
                .findAllUserTeamsByUserID(userId)
                .stream()
                .map(UserTeam::getUser)
                .filter(u -> (u.getFirstName().toLowerCase().contains(query.toLowerCase())
                        || u.getLastName().toLowerCase().contains(query.toLowerCase()))
                        && !Objects.equals(u.getId(), userId))
                .map(UserSearchDTO::new)
                .toList();
    }

    public void setFirstAccessNull(Long userId) {
        User user = findById(userId);
        user.setFirstAccess(!user.isFirstAccess());
        userRepository.save(user);
    }

    public void changePasswordPeriodically(ChangePasswordDTO changePasswordDTO){
        User user = findById(changePasswordDTO.getId());
        if(!Objects.equals(user.getPassword(), changePasswordDTO.getPassword())){
            if(regexValidate.isPasswordSecure(changePasswordDTO)) {
                user.setPassword(new BCryptPasswordEncoder().encode(changePasswordDTO.getPassword()));
                user.setRegisterDay(LocalDateTime.now());
                save(user);
            }
        } else {
            throw new RuntimeException("Your password can't be the same as the last");
        }
    }



}
