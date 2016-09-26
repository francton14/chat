package com.piczon.services.impl;

import com.piczon.data.dao.MessageDao;
import com.piczon.data.dao.UserDao;
import com.piczon.data.dao.predicates.PredicateBuilder;
import com.piczon.data.dto.MessageCreateRequest;
import com.piczon.data.dto.MessageDeleteResponse;
import com.piczon.data.dto.MessageShowResponse;
import com.piczon.data.entities.Message;
import com.piczon.services.ChatService;
import com.piczon.services.utils.EntityDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by franc on 9/19/2016.
 */
@Service("chatService")
@Transactional
public class ChatServiceImpl implements ChatService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    @Override
    public String chat(String username, Model model) {
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        model.addAttribute("user", userDao.findOne(userPredicate.getResult()));
        return "chat";
    }

    @Override
    public ResponseEntity<?> index() {
        List<MessageShowResponse> messages = new ArrayList<>();
        messageDao.findAll().forEach(message -> {
            messages.add(EntityDtoConverter.entityToDto(message));
        });
        return ResponseEntity.ok(messages);
    }

    @Override
    public MessageShowResponse create(MessageCreateRequest request, String username) {
        MessageShowResponse messageShowResponse = new MessageShowResponse();
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        Message message = new Message();
        message.setText(request.getText());
        message.setTimeStamp(new Date());
        message.setUser(userDao.findOne(userPredicate.getResult()));
        message = messageDao.saveAndFlush(message);
        if (message.getId() != null) {
            messageShowResponse = EntityDtoConverter.entityToDto(message);
            messageShowResponse.setStatus("ok");
        } else {
            messageShowResponse.setStatus("some_problems_occurred");
        }
        return messageShowResponse;
    }

    @Override
    public MessageDeleteResponse delete(Long messageId, String username) {
        MessageDeleteResponse messageDeleteResponse = new MessageDeleteResponse();
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        PredicateBuilder.Finish messagePredicate = PredicateBuilder.MESSAGE.builder().where("id", messageId).and("user", userDao.findOne(userPredicate.getResult())).finish();
        if (messageDao.exists(messagePredicate.getResult())) {
            messageDao.delete(messageId);
            messageDeleteResponse.setMessageId(messageId);
            messageDeleteResponse.setStatus("ok");
        } else {
            messageDeleteResponse.setStatus("message_not_found");
        }
        return messageDeleteResponse;
    }

}
