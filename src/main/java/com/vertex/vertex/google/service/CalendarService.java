package com.vertex.vertex.google.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.vertex.vertex.project.model.DTO.ProjectCreateDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.task.model.DTO.TaskModeViewDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.enums.CreationOrigin;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import com.vertex.vertex.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CalendarService {

    private final ProjectService projectService;
    private final UserRepository userRepository;
    private final TaskService taskService;


    public List<Task> convertEventsToTask(HttpServletResponse response, Long userId, Long projectId) {
        try {
            HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service =
                    new Calendar.Builder(HTTP_TRANSPORT, CalendarConfig.JSON_FACTORY, getCredentials(response, userId))
                            .setApplicationName(CalendarConfig.APPLICATION_NAME)
                            .build();

            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = service.events().list("primary")
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            if (!items.isEmpty()) {
                return taskService.convertEventsToTask(items, projectId, userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Empty!");
    }



    public Project createCalendarProject(
            Long teamId, ProjectCreateDTO dto,
            Long userId, HttpServletResponse response) {
        Project project = projectService.findById(projectService.saveWithRelationOfProject(dto, teamId).getId());
        project.setCreationOrigin(CreationOrigin.GOOGLE);
        return projectService.save(project);
    }


    public Project findByIdAndGetNewEvents(Long projectId, Long userId, HttpServletResponse response) {
        Project project = projectService.findById(projectId);
        project.getTasks().addAll(convertEventsToTask(response, userId, projectId));
        return projectService.save(project);
    }

    public Task create(HttpServletResponse response, Long userId, Long projectId)
            throws GeneralSecurityException, IOException {
        User user = userRepository.findById(userId).get();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service =
                new Calendar.Builder(HTTP_TRANSPORT, CalendarConfig.JSON_FACTORY, getCredentials(response, userId))
                        .setApplicationName(CalendarConfig.APPLICATION_NAME)
                        .build();

        Event event = new Event()
                .setSummary("Nova tarefa")
                .setDescription("Descreva um pouco sobre sua tarefa aqui.");

        DateTime startDateTime = new DateTime(LocalDateTime.now().toString());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Sao_Paulo");
        event.setStart(start);

        DateTime endDateTime = new DateTime(LocalDateTime.now().plusHours(1).toString());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Sao_Paulo");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail(user.getEmail()),
                new EventAttendee().setEmail("vertex.workspacee@gmail.com")
        };
        event.setAttendees(Arrays.asList(attendees));

        String calendarId = "primary";
        event = service.events().insert(calendarId, event).execute();
        return taskService.saveNewEvent(event, user, projectId);
    }


}
