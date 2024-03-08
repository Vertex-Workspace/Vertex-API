package com.vertex.vertex.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertex.vertex.chat.relations.message.Message;
import io.micrometer.common.lang.NonNullApi;
import lombok.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session);
        webSocketSessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        System.out.println("PAYLOAD: " + message.getPayload());

        for (WebSocketSession webSocketSession : webSocketSessions){
            webSocketSession.sendMessage(message);
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        byte[] imageData = message.getPayload().array();

        System.out.println("ImageData = " + imageData);

        BinaryMessage binaryMessage = new BinaryMessage(imageData);

        System.out.println("BinaryMessage = " + binaryMessage);

        for (WebSocketSession webSocketSession : webSocketSessions){
            try {
                webSocketSession.sendMessage(binaryMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        webSocketSessions.remove(session);
    }

}
