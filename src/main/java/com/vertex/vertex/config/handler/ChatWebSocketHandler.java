package com.vertex.vertex.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertex.vertex.chat.relations.message.Message;
import io.micrometer.common.lang.NonNullApi;
import lombok.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session);
        webSocketSessions.add(session);
    }

//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//
//        System.out.println("PAYLOAD: " + message);
//
//        for (WebSocketSession webSocketSession : webSocketSessions) {
//            webSocketSession.sendMessage(message);
//        }
//    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        System.out.println(message);

        byte[] data = Base64.getDecoder().decode((byte[]) message.getPayload());
        System.out.println(data);
        System.out.println(Arrays.toString(data));
        for (WebSocketSession webSocketSession : webSocketSessions) {
            try {
                webSocketSession.sendMessage(new TextMessage(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        webSocketSessions.remove(session);
    }

}
