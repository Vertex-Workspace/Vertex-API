package com.vertex.vertex.google.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.vertex.vertex.project.model.DTO.ProjectCreateDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.enums.CreationOrigin;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class CalendarService {

    private final UserRepository userRepository;
    private final TaskService taskService;
    private final ProjectService projectService;

    public Project createCalendarProject(
            Long teamId, ProjectCreateDTO dto,
            Long userId, HttpServletResponse response) {
        Project project = projectService.findById(projectService.saveWithRelationOfProject(dto, teamId).getId());
        project.setCreationOrigin(CreationOrigin.GOOGLE);
        return projectService.save(project);
    }



    public List<Task> convertEventsToTask(Long userId, Long projectId, List<Event> items) {
        try {
            if (!items.isEmpty()) {
                return taskService.convertEventsToTask(items, projectId, userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Empty!");
    }

    public List<Event> getCalendarItems() {
        try {
            Calendar service = CalendarConfig.createCalendar();

            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = service.events().list("primary")
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            return events.getItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Project findByIdAndGetNewEvents(Long projectId, Long userId, HttpServletResponse response) {
        Project project = projectService.findById(projectId);
        project.getTasks().addAll(convertEventsToTask(userId, projectId, getCalendarItems()));
        updateCalendarTasks(project, getCalendarItems());
        return projectService.save(project);
    }

    private void updateCalendarTasks(Project project, List<Event> items) {
        items.stream()
                .filter(i -> taskService.existsByGoogleId(i.getId()))
                .forEach(item -> {
                    Task task = taskService.findByGoogleId(item.getId());
                    if (!task.getName().equals(item.getSummary())
                            || !task.getDescription().equals(item.getDescription())) {
                        task.setDescription(item.getDescription());
                        task.setName(item.getSummary());
                        taskService.save(task);
                    }
                });
    }

    public Task create(HttpServletResponse response, Long userId, Long projectId)
            throws IOException, GeneralSecurityException {
        Calendar service = CalendarConfig.createCalendar();
        User user = userRepository.findById(userId).get();

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
