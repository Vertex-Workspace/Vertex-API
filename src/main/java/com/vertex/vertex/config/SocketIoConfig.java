//package com.vertex.vertex.config;
//
//import com.corundumstudio.socketio.AckRequest;
//import com.corundumstudio.socketio.listener.DataListener;
//import com.vertex.vertex.chat.relations.message.Message;
//import jakarta.annotation.PreDestroy;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//
//import com.corundumstudio.socketio.Configuration;
//import com.corundumstudio.socketio.SocketIOClient;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.listener.ConnectListener;
//import com.corundumstudio.socketio.listener.DisconnectListener;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.CrossOrigin;
//
//import java.io.InputStream;
//
//@CrossOrigin
//@Log4j2
//@Component
//public class SocketIoConfig {
//
//    @Value("${socket.host}")
//    private String SOCKETHOST;
//    @Value("${socket.port}")
//    private int SOCKETPORT;
//    private SocketIOServer server;
//
//    @Bean
//    public SocketIOServer socketIOServer() {
//        Configuration config = new Configuration();
//        config.setHostname(SOCKETHOST);
//        config.setPort(SOCKETPORT);
//        server = new SocketIOServer(config);
//        server.start();
//
//        server.addConnectListener(new ConnectListener() {
//            @Override
//            public void onConnect(SocketIOClient client) {
//
//                log.info("new user connected with socket " + client.getSessionId());
//            }
//        });
//
//        server.addDisconnectListener(new DisconnectListener() {
//            @Override
//            public void onDisconnect(SocketIOClient client) {
//                client.getNamespace().getAllClients().stream().forEach(data -> {
//                    log.info("user disconnected " + data.getSessionId().toString());
//                });
//            }
//        });
//        return server;
//    }
//
//    @PreDestroy
//    public void stopSocketIOServer() {
//        this.server.stop();
//    }
//
//}