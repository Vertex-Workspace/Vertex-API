package com.vertex.vertex.user.service;

import com.vertex.vertex.log.model.exception.EntityDoesntExistException;
import com.vertex.vertex.notification.service.NotificationService;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.model.DTO.TaskSearchDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_hours.service.TaskHoursService;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.service.GroupService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.DTO.*;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.model.exception.*;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import com.vertex.vertex.user.relations.personalization.service.PersonalizationService;
import com.vertex.vertex.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.*;
import java.util.function.Function;
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

    public User save(User user){
        return userRepository.save(user);
    }

    public User save(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        if(existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException();
        }

        if (regexValidate.isEmailSecure(user.getEmail())) {
            user.setEmail(user.getEmail());
        }
        else {
            throw new InvalidEmailException();
        }
        regexValidate.isPasswordSecure(user,userDTO);

        userSetDefaultInformations(user);

        byte[] data = Base64.getDecoder().decode(userDTO.getImage());
        user.setImage(data);

        return userRepository.save(user);
    }

    public void userSetDefaultInformations(User user){
        user.setLocation("Brasil");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setLocation("Brasil");
        user.setPersonalization(personalizationService.defaultSave(user));
        user.setTaskReview(true);
        user.setNewMembersAndGroups(true);
        user.setPermissionsChanged(true);
        user.setSendToEmail(true);
        user.setAnyUpdateOnTask(true);
        user.setResponsibleInProjectOrTask(true);
    }

    public User findByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        }
        throw new InvalidEmailException();
    }


    public boolean existsByEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    private void emailValidation(User user){
        if(existsByEmail(user.getEmail())){
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
        BeanUtils.copyProperties(userEditionDTO, user);
        emailValidation(user);
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

    public UserPublicProfileDTO findUserInformations(Long id, Long loggedUserID) {
        User user = findById(id);
        UserPublicProfileDTO dto = new UserPublicProfileDTO();
        mapper.map(user, dto);
        dto.setFullname(user.getFullName());

        //All tasks that the user is responsible
        List<TaskResponsable> tasksResponsible =
                teamService.findAllUserTeamsByUserID(user.getId()).stream()
                        .flatMap(userTeam -> userTeam.getTeam().getProjects().stream())
                        .flatMap(project ->  project.getTasks().stream())
                        .flatMap(task -> task.getTaskResponsables().stream())
                        .filter(taskResponsable -> taskResponsable.getUserTeam().getUser().getId().equals(id))
                        .toList();


        List<PropertyListKind> listKinds = List.of(PropertyListKind.TODO, PropertyListKind.DOING, PropertyListKind.DONE);
        for (PropertyListKind propertyListKind : listKinds){
            int sumFinal = 0;
            for (TaskResponsable taskResponsable : tasksResponsible){
                sumFinal += taskResponsable.getTask().getValues()
                        .stream()
                        .filter(value -> value.getProperty().getKind().equals(PropertyKind.STATUS)
                                && ((PropertyList) value.getValue()).getPropertyListKind().equals(propertyListKind))
                        .toList().size();
            }
            dto.getTasksPerformances().add(sumFinal);
        }

        Duration duration = Duration.ZERO;
        for (TaskResponsable taskResponsable : tasksResponsible){
            duration = duration.plus(taskHoursService.calculateTimeOnTask(taskResponsable));
        }
        dto.setTime(LocalTime.MIDNIGHT.plus(duration));


        User loggedUser = findById(loggedUserID);

        if(!loggedUser.equals(user)){

            //Refatorar obrigatoriamente - ass. Otávio
            List<Task> tasks = tasksResponsible.stream().map(TaskResponsable::getTask).toList();
            List<TaskResponsable> taskResponsables = tasks.stream()
                    .flatMap(task -> task.getTaskResponsables().stream())
                    .filter(taskResponsable -> taskResponsable.getUserTeam().getUser().getId().equals(loggedUserID))
                    .toList();
            dto.setTasksInCommon(taskResponsables.stream()
                    .map(TaskResponsable::getTask)
                    .map(TaskSearchDTO::new).toList());
        }

        return dto;
    }


    public void deleteById(Long id) {
        if (!getUserRepository().existsById(id)) {
            throw new UserNotFoundException();
        } else {
            userRepository.deleteById(id);
        }
    }


    public void saveImage(MultipartFile imageFile, Long id) throws IOException {
        try {
            User user = findById(id);
            user.setImage(imageFile.getBytes());
            userRepository.save(user);
        } catch (Exception ignored) {}
    }

    public User patchUserPersonalization(Long id, Personalization personalization){
        User user = findById(id);
        personalization.setId(user.getPersonalization().getId());
        personalization.setUser(user);
        user.setPersonalization(personalization);
        return userRepository.save(user);
    }
    public User patchUserPassword(UserLoginDTO userLoginDTO){
        User user = findByEmail(userLoginDTO.getEmail());
        user.setPassword(userLoginDTO.getPassword());
        return userRepository.save(user);
    }
    public List<User> getUsersByGroup(Long groupId){
        List<User> users = new ArrayList<>();
        Group group = groupService.findById(groupId);

        for (int i = 0; i < group.getUserTeams().size(); i++) {
                users.add(group.getUserTeams().get(i).getUser());
        }
       return users;
    }
    public Boolean imageUpload(Long id, MultipartFile file){
        User user;
        try {
            if (userRepository.existsById(id)) {
                user = findById(id);
                user.setImage(file.getBytes());
                userRepository.save(user);
                return true;
            }
        } catch (Exception ignored) {
            throw new RuntimeException("Erro");
        }
        throw new RuntimeException();
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
}
