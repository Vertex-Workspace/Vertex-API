package com.vertex.vertex.user.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class ForgotPasswordService {

    public Long sendCodeToEmail(String emailTo) {
        Properties properties = System.getProperties();

        // define os dados básicos
        String host = "smtp.gmail.com";
        String email = "vertex.workspacee@gmail.com";
        String password = "sryx rozm wixy iool";

        //liga os protocolo tudo
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //cria a sessão
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        //mostra onde tá os erros
        session.setDebug(true);

        Long code = generateCode();

        try {
            MimeMessage message = new MimeMessage(session);
            // Configurando quem envia
            message.setFrom(new InternetAddress(email, "VERTEX: Gerencie seu tempo de ponta a ponta!"));
            // Configurando quem recebe
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            // Configurando o assunto
            message.setSubject("O código de recuperação da sua conta VERTEX é: " + code + "\n");

            // Construindo o conteúdo HTML do e-mail
            String htmlContent = "<html><body style='background-color: #f6f8fa;'>"+
            "<div style='margin: 0 auto; max-width: 600px; padding: 20px; text-align: center;'>"+
//            "<img src='https://github.com/Vertex-Workspace/Vertex/blob/main/src/assets/vertex-logo.png?raw=true' alt='VERTEX LOGO' style='margin-bottom: 20px;'>"+
            "<div style='background-color: #fff; border-radius: 6px; padding: 40px;'>"+
            "<h2 style='color: #092C4C; font-weight: 500;'>Código de Recuperação</h2>"+
            "<p style='font-size: 16px;'>Seu código de recuperação é:</p>"+
            "<h1 style='font-size: 36px; color: #092C4C; margin-top: 10px; margin-bottom: 30px;'>" + code + "</h1>"+
            "<p style='font-size: 14px; color: #586069;'>Se você não solicitou esta redefinição de senha, você pode ignorar este email.</p>"+
            "</div>"+
            "<p style='font-size: 12px; color: #586069; margin-top: 20px;'>Este é um email automático. Por favor, não responda.</p>"+
            "</div>"+
            "</body></html>";

            // Configurando o conteúdo do e-mail como HTML
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return code;
    }

    public Long generateCode() {
        return (long) (Math.random() * 1000000);
    }
}
