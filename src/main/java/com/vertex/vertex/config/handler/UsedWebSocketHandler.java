package com.vertex.vertex.config.handler;

import com.vertex.vertex.notification.entity.NotificationWebSocketDTO;
import com.vertex.vertex.notification.entity.model.Notification;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class UsedWebSocketHandler extends AbstractWebSocketHandler {

    public static final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession webSocketSession : webSocketSessions) {
            if(Objects.requireNonNull(webSocketSession.getUri()).getPath().contains("chat")) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessions.remove(session);
    }

    public void sendNotification(Long userID) throws IOException {
        for (WebSocketSession webSocketSession : webSocketSessions) {
            if(Objects.requireNonNull(webSocketSession.getUri()).getPath().contains("notifications")){
                webSocketSession.sendMessage(new TextMessage(userID.toString()));
            }
        }
    }


}
