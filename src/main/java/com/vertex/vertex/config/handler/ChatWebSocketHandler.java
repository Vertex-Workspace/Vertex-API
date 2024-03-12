package com.vertex.vertex.config.handler;

import com.vertex.vertex.chat.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ChatWebSocketHandler extends AbstractWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession webSocketSession : webSocketSessions) {
            webSocketSession.sendMessage(message);
        }
    }

//    @Override
//    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
////        System.out.println(message);
////        byte[] data = message.getPayload().array();
//        for (WebSocketSession webSocketSession : webSocketSessions) {
//            try {
//                webSocketSession.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessions.remove(session);
    }

}
