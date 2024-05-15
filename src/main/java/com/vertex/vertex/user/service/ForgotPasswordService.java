package com.vertex.vertex.user.service;


import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

@Service
@AllArgsConstructor
public class ForgotPasswordService {

    private final UserService userService;

    public Long sendCodeToEmail(String emailTo) {
        userService.findByEmail(emailTo);

        Properties properties = new Properties();

        String host = "smtp.gmail.com";
        String email = "vertex.workspacee@gmail.com";
        String password = "sryx rozm wixy iool";

        System.out.println("Sending email to: " + emailTo);

        // Configuração das propriedades do servidor SMTP
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", host);

        // Cria a sessão com autenticação
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        // Ativa o modo de depuração para visualizar possíveis erros
        session.setDebug(true);

        // Geração do código de recuperação
        Long code = generateCode();

        try {
            // Criação da mensagem de e-mail
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email, "VERTEX: Gerencie seu tempo de ponta a ponta!"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            message.setSubject("O código de recuperação da sua conta VERTEX é: " + code + "\n");

            // Criação do conteúdo HTML do e-mail
            MimeMultipart multipart = new MimeMultipart();

            // Texto do e-mail
            MimeBodyPart textPart = new MimeBodyPart();
            String htmlContent = "<html><body style='background-color: #f6f8fa;'>" +
                    "<div style='margin: 0 auto; max-width: 600px; padding: 20px; text-align: center;'>" +
                    "<img style='width: 140px; height: auto; margin-bottom: 20px;' src='cid:logo' alt='VERTEX LOGO' >" +
                    "<div style='background-color: #fff; border-radius: 6px; padding: 40px;'>" +
                    "<h2 style='color: #092C4C; font-weight: 500;'>Código de Recuperação</h2>" +
                    "<p style='font-size: 16px;'>Seu código de recuperação é:</p>" +
                    "<h1 style='font-size: 36px; color: #092C4C; margin-top: 10px; margin-bottom: 30px;'>" + code + "</h1>" +
                    "<p style='font-size: 14px; color: #586069;'>Se você não solicitou esta redefinição de senha, você pode ignorar este email.</p>" +
                    "</div>" +
                    "<p style='font-size: 12px; color: #586069; margin-top: 20px;'>Este é um email automático. Por favor, não responda.</p>" +
                    "</div>" +
                    "</body></html>";
            textPart.setContent(htmlContent, "text/html; charset=utf-8");
            multipart.addBodyPart(textPart);

            // Anexo da imagem da empresa em base64
            MimeBodyPart imagePart = new MimeBodyPart();
            String imageData = this.getImageData(); // ESSA LINHA ESTÁ DANDO ERRO / APÓS ESSA LINHA O CÓDIGO NÃO EXECUTA
            imagePart.setDataHandler(new DataHandler(new ByteArrayDataSource(imageData, "image/png")));
            imagePart.setHeader("Content-ID", "<logo>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);

            // Define o conteúdo do e-mail
            message.setContent(multipart);

            // Envio do e-mail
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }

        return code;
    }

    private Long generateCode() {
        return (long) (Math.random() * 1000000);
    }

    private String getImageData() throws IOException {
        // Ler a imagem e converter para base64
        InputStream inputStream = getClass().getResourceAsStream("/vertex-logo.png");
        byte[] imageData = IOUtils.toByteArray(inputStream);
        return Base64.encodeBase64String(imageData);
    }

    // Classe auxiliar para criar um DataSource a partir de uma string de dados
    static class ByteArrayDataSource implements DataSource {
        private final byte[] data;
        private final String type;

        public ByteArrayDataSource(String data, String type) {
            this.data = Base64.decodeBase64(data);
            this.type = type;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(data);
        }

        @Override
        public OutputStream getOutputStream() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getContentType() {
            return type;
        }

        @Override
        public String getName() {
            return "ByteArrayDataSource";
        }
    }
}