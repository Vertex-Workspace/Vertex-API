package com.vertex.vertex.notification.entity.service;


import com.vertex.vertex.config.handler.UsedWebSocketHandler;
import com.vertex.vertex.notification.entity.NotificationWebSocketDTO;
import com.vertex.vertex.notification.entity.model.LogRecord;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.repository.LogRepository;
import com.vertex.vertex.notification.repository.NotificationRepository;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UsedWebSocketHandler usedWebSocketHandler;
    private final LogRepository logRepository;
    private final ModelMapper mapper;

    public Notification save(Notification notification) {
        Notification notificationSaved = notificationRepository.save(notification);
        if(notification.getUser().getSendToEmail()){
            sendToEmail(notificationSaved);
        }
        webSocket(notification.getUser().getId());
        return notificationSaved;
    }

    public LogRecord saveLogRecord(Task task, String description, UserTeam ut) {
        return logRepository.save(
                new LogRecord(task, description, ut));

    }
    public LogRecord saveLogRecord(Task task, String description) {
        return logRepository.save(
                new LogRecord(task, description));
    }

    public Notification update(Notification notification){
        return notificationRepository.save(notification);
    }

    public void delete(Notification notification) {
        notificationRepository.delete(notification);
    }


    public List<Notification> getNotificationsByUser(Long userID) {
        return notificationRepository.findAllByUser_IdOrderByIdDesc(userID);
    }

    public void groupAndTeam(String title, UserTeam userTeam) {
        Notification notification = new Notification(
                userTeam.getTeam(),
                title,
                "equipe/" + userTeam.getTeam().getId(),
                userTeam.getUser()
        );
        save(notification);
    }

    public void webSocket(Long userID){
        try {
            usedWebSocketHandler.sendNotification(userID);
        } catch (IOException ignored) {}
    }

    private void sendToEmail(Notification notification) {
//        try {
//
//            Properties properties = System.getProperties();
//
//            // define os dados básicos
//            String host = "smtp.gmail.com";
//            String email = "vertex.workspacee@gmail.com";
//            String password = "sryx rozm wixy iool";
//
//            //liga os protocolo tudo
//            properties.put("mail.smtp.host", host);
//            properties.put("mail.smtp.port", "465");
//            properties.put("mail.smtp.auth", "true");
//            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//
//            //cria a sessão
//            Session session = Session.getDefaultInstance(properties, new Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(email, password);
//                }
//            });
//
//            //mostra onde tá os erros
//            session.setDebug(true);
//
//            Long code = (long) (Math.random() * 1000000);
//
//            try {
//                MimeMessage message = new MimeMessage(session);
//                // Configurando quem envia
//                message.setFrom(new InternetAddress(email, "VERTEX: Gerencie seu tempo de ponta a ponta!"));
//                // Configurando quem recebe
//                message.addRecipient(Message.RecipientType.TO, new InternetAddress(notification.getUser().getEmail()));
//                // Configurando o assunto
//                message.setSubject("O código de recuperação da sua conta VERTEX é: " + code + "\n");
//
//                // Construindo o conteúdo HTML do e-mail
//                String htmlContent = "<html><body style='background-color: #f6f8fa;'>" +
//                        "<div style='margin: 0 auto; max-width: 600px; padding: 20px; text-align: center;'>" +
////            "<img src='https://github.com/Vertex-Workspace/Vertex/blob/main/src/assets/vertex-logo.png?raw=true' alt='VERTEX LOGO' style='margin-bottom: 20px;'>"+
//                        "<div style='background-color: #fff; border-radius: 6px; padding: 40px;'>" +
//                        "<p style='font-size: 14px; color: #586069;'>" + notification.getTeamName() + " | " + notification.getProjectName() + "</p>" +
//                        "<h2 style='color: #092C4C; font-weight: 500;'>" + notification.getTitle() + "</h2>" +
//                        "<h1 style='font-size: 36px; color: #092C4C; margin-top: 10px; margin-bottom: 30px;'>" + notification.getDate().toString() + "</h1>" +
//                        "</div>" +
//                        "</div>" +
//                        "</body></html>";
//
//                // Configurando o conteúdo do e-mail como HTML
//                message.setContent(htmlContent, "text/html; charset=utf-8");
//
//                Transport.send(message);
//            } catch (MessagingException mex) {
//                mex.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                throw new RuntimeException(e);
//            }
//        } catch (Exception e) {
//            System.out.println("Falha E-mail");
//        }

    }


}
