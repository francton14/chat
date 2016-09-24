package com.piczon.endpoints.controllers;

import com.piczon.data.dto.MessageCreateRequest;
import com.piczon.data.dto.MessageDeleteResponse;
import com.piczon.data.dto.MessageShowResponse;
import com.piczon.data.entities.Message;
import com.piczon.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Created by franc on 9/20/2016.
 */
@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @RequestMapping()
    public String chat(Model model, Principal principal) {
        return chatService.chat(principal.getName(), model);
    }

    @RequestMapping("/messages")
    public ResponseEntity<?> index() {
        return chatService.index();
    }

    @MessageMapping("/new_message")
    @SendTo("/notifications/new_message")
    public MessageShowResponse create(MessageCreateRequest request, Principal principal) {
        return chatService.create(request, principal.getName());
    }

    @MessageMapping("/delete_message")
    @SendTo("/notifications/delete_message")
    public MessageDeleteResponse delete(Long messageId, Principal principal) {
        return chatService.delete(messageId, principal.getName());
    }

}
