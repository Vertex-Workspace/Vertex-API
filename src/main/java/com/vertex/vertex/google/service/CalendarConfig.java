package com.vertex.vertex.google.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.vertex.vertex.user.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Configuration
@RestController
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

        GoogleAuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
        authorizationUrl.setRedirectUri("http://localhost:7777/Callback");
        authorizationUrl.setAccessType("offline");
        String url = authorizationUrl.build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(userId.toString());
//        //returns an authorized Credential object.
            return credential;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String generateUrl() {
        try {
            Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
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

            GoogleAuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
            authorizationUrl.setRedirectUri("http://localhost:7777/Callback");
            authorizationUrl.setAccessType("offline");

            return authorizationUrl.build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/calendar/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        System.out.println("chegou aqui");
        System.out.println(generateUrl());
        response.sendRedirect(generateUrl());
    }

    @GetMapping("/Callback")
    public ResponseEntity<?> callback(HttpServletResponse response, HttpServletRequest request) {
        String code = request.getParameter("code");
        if (code != null) {
            try {
                Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
                Credential credential = exchangeCodeForToken(code, userId);
          
                response.sendRedirect("http://localhost:4200");
                return ResponseEntity.ok("Bem-sucedido");
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autorizar: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de autorização ausente.");
        }
    }

    private Credential exchangeCodeForToken(String code, Long userId) throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        File in = new File(CalendarConfig.CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(CalendarConfig.JSON_FACTORY, new InputStreamReader(new FileInputStream(in)));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, CalendarConfig.JSON_FACTORY, clientSecrets, CalendarConfig.SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(CalendarConfig.TOKENS_DIRECTORY_PATH + "/" + userId.toString())))
                .setAccessType("offline")
                .build();

        GoogleAuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code);
        tokenRequest.setRedirectUri("http://localhost:7777/Callback");
        GoogleTokenResponse tokenResponse = tokenRequest.execute();

        return flow.createAndStoreCredential(tokenResponse, userId.toString());
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
