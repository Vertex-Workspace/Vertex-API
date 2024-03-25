//package com.vertex.vertex.chat.controller;
//
//import com.corundumstudio.socketio.AckRequest;
//import com.corundumstudio.socketio.SocketIOClient;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.listener.ConnectListener;
//import com.corundumstudio.socketio.listener.DataListener;
//import com.corundumstudio.socketio.listener.DisconnectListener;
//import com.vertex.vertex.chat.relations.message.Message;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//@Log4j2
//public class SocketIoController {
//
//    @Autowired
//    public SocketIOServer socketServer;
//
//    @Bean
//    public SocketIoController socketIoController(){
//        return this;
//    }
//
//    public SocketIoController(SocketIOServer socketServer){
//        this.socketServer=socketServer;
//
//        this.socketServer.addConnectListener(onUserConnectWithSocket);
//        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);
//
//        /**
//         * Here we create only one event listener
//         * but we can create any number of listener
//         * messageSendToUser is socket end point after socket connection user have to send message payload on messageSendToUser event
//         */
//        this.socketServer.addEventListener("chat", Message.class, onSendMessage);
//
//    }
//
//
//    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
//        @Override
//        public void onConnect(SocketIOClient client) {
//            log.info("Perform operation on user connect in controller");
//        }
//    };
//
//
//    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
//        @Override
//        public void onDisconnect(SocketIOClient client) {
//            log.info("Perform operation on user disconnect in controller");
//        }
//    };
//
//
//    public DataListener<Message> onSendMessage = new DataListener<Message>() {
//        @Override
//        public void onData(SocketIOClient client, Message message, AckRequest acknowledge) throws Exception {
//
//            System.out.println("LJkfjlksjdkjd " + message);
//
//            socketServer.getBroadcastOperations().sendEvent("chat",client, message);
//            acknowledge.sendAckData("Message send to target user successfully");
//        }
//    };
//
//}