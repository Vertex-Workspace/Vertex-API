package com.vertex.vertex.chat.controller;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatController")
@AllArgsConstructor
@Data
public class ChatController {

    private final ChatService chatService;
//    @PostMapping
//    public ResponseEntity<Chat> create(Long id){
//        try {
//            chatService.create(id);
//            return new ResponseEntity<>( HttpStatus.CREATED);
//        }catch (Exception e){
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//    }

    @GetMapping()
    public ResponseEntity<List<Chat>> findAllChats(){
        try {
            return new ResponseEntity<>(chatService.findAll(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
