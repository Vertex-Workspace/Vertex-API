package com.vertex.vertex.google.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
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
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.user.model.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CalendarManagerService {

    private final TaskRepository taskService;
    private final Calendar service;


    public void update(HttpServletResponse response, Task task) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service =
                new Calendar.Builder(HTTP_TRANSPORT, CalendarConfig.JSON_FACTORY, CalendarConfig.getCredentials(response,
                        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
                ))
                        .setApplicationName(CalendarConfig.APPLICATION_NAME)
                        .build();

        Event event = service.events().get
                        ("primary", task.getGoogleId())
                .execute();
        event.setSummary(task.getName());
        event.setDescription(task.getDescription());

        LocalDateTime local = ((LocalDateTime) task.getValues().get(1).getValue());

        DateTime startDateTime = new DateTime(local.atZone(ZoneId.of("America/Sao_Paulo")).toInstant().toEpochMilli());

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Sao_Paulo");
        event.setStart(start);

        DateTime endDateTime = new DateTime(local.plusHours(1).atZone(ZoneId.of("America/Sao_Paulo")).toInstant().toEpochMilli());

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Sao_Paulo");
        event.setEnd(end);

        service.events().update("primary", event.getId(), event).execute();
    }

    public void delete(HttpServletResponse response, Task task) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service =
                new Calendar.Builder(HTTP_TRANSPORT, CalendarConfig.JSON_FACTORY, getCredentials(response,
                        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
                ))
                        .setApplicationName(CalendarConfig.APPLICATION_NAME)
                        .build();

        Event event = service.events().get
                        ("primary", task.getGoogleId())
                .execute();
        service.events().delete("primary", event.getId()).execute();

    }

}
