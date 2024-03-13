package com.vertex.vertex.chat.controller;


import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.chat.service.ChatService;
import com.vertex.vertex.config.handler.ChatWebSocketHandler;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/chatController")
@AllArgsConstructor
@Data
public class ChatController {

    private final ChatService chatService;

    @PatchMapping("/chat/{idChat}")
    public ResponseEntity<Chat> patchChat(@PathVariable Long idChat, @RequestBody UserTeamAssociateDTO userTeam){
        try {
            System.out.println(userTeam);
            this.chatService.patchUserTeams(idChat,userTeam);
            return new ResponseEntity<>( HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("messagesByChatId/{idChat}")
    public ResponseEntity<List<Message>> findAllMessagesByChatId(@PathVariable Long idChat){

        try {
            return new ResponseEntity<>(chatService.findMessagesByChatId(idChat),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }


//    @MessageMapping("/{idChat}/{idUser}")
//    @SendTo("/{idChat}")
    @PatchMapping("/messagePatch/{idChat}/{idUser}")
    public ResponseEntity<Chat> patchMessages(@PathVariable Long idChat,@PathVariable Long idUser, @RequestBody Message message){


        System.out.println("a"+idChat);
        try {
            return new ResponseEntity<>(chatService.patchMessages(idChat,idUser,message),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("patchFile/{chatId}")
    public Chat patchFileOnChat(
            @PathVariable Long chatId,
            @RequestParam MultipartFile file,
            @RequestParam String user
//            @RequestParam LocalDateTime time
    ) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException();
        }
        return chatService.saveFile(chatId,file,user);
    }

    @GetMapping()
    public ResponseEntity<List<Chat>> findAllChats(){
        try {
            return new ResponseEntity<>(this.chatService.findAll(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


}
