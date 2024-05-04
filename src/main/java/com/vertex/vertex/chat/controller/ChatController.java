package com.vertex.vertex.chat.controller;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.model.DTO.ChatListDTO;
import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.chat.service.ChatService;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/chatController")
@AllArgsConstructor
@Data
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @PatchMapping("/chat/{idChat}")
    public ResponseEntity<Chat> patchChat(@PathVariable Long idChat, @RequestBody UserTeamAssociateDTO userTeam){
        try {
            chatService.patchUserTeams(idChat,userTeam);
            return new ResponseEntity<>( HttpStatus.CREATED);
        }catch (Exception e){
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


    @PatchMapping("/messagePatch/{idChat}/{idUser}")
    public ResponseEntity<Chat> patchMessages(@PathVariable Long idChat,@PathVariable Long idUser,
                                              @RequestBody Message message){
        System.out.println("a"+idChat);
        try {
            return new ResponseEntity<>(chatService.patchMessages(idChat,userService.findById(idUser),message),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("patchFile/{chatId}")
    public ResponseEntity<?> patchFileOnChat(
            @PathVariable Long chatId,
            @RequestParam MultipartFile file,
            @RequestParam String user
    ) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(chatService.saveFile(chatId,file,user), HttpStatus.OK);
    }

    @GetMapping("/allChatsOfUser/{id}")
    public ResponseEntity<List<ChatListDTO>> findAllChatsByUser(@PathVariable Long id){
        try {
            return new ResponseEntity<>(chatService.findAllByUser(id) ,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


}