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
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.vertex.vertex.user.model.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Configuration
@AllArgsConstructor
public class CalendarConfig {

    protected static final String APPLICATION_NAME = "Vertex";
    protected static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    protected static final String TOKENS_DIRECTORY_PATH = "tokens";
    protected static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);
    protected static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";

    protected static Credential getCredentials(Long userId)
            throws IOException {

        try {
            HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            // Load client secrets.
            File in = new File(CalendarConfig.CREDENTIALS_FILE_PATH);
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(CalendarConfig.JSON_FACTORY, new InputStreamReader(new FileInputStream(in)));

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, CalendarConfig.JSON_FACTORY, clientSecrets, CalendarConfig.SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CalendarConfig.TOKENS_DIRECTORY_PATH + "/" + userId.toString())))
                    .setAccessType("offline")
                    .build();

//        GoogleAuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
//        authorizationUrl.setRedirectUri("http://localhost:8888/Callback");
//        authorizationUrl.setAccessType("offline");
//        String url = authorizationUrl.build();
//        response.sendRedirect(url);

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(userId.toString());
//        //returns an authorized Credential object.
            return credential;
//            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Calendar createCalendar()
            throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return
                new Calendar.Builder(HTTP_TRANSPORT, CalendarConfig.JSON_FACTORY, CalendarConfig.getCredentials(
                        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
                ))
                        .setApplicationName(CalendarConfig.APPLICATION_NAME)
                        .build();
    }

}
