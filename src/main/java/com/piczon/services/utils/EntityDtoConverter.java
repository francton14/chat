package com.piczon.services.utils;

import com.piczon.data.dto.MessageShowResponse;
import com.piczon.data.dto.UserShow;
import com.piczon.data.entities.Message;
import com.piczon.data.entities.User;

/**
 * Created by franc on 9/19/2016.
 */
public class EntityDtoConverter {

    public static UserShow entityToDto(User user) {
        UserShow userShow = new UserShow();
        userShow.setId(user.getId());
        userShow.setUsername(user.getUsername());
        userShow.setFirstName(user.getFirstName());
        userShow.setLastName(user.getLastName());
        return userShow;
    }

    public static MessageShowResponse entityToDto(Message message) {
        MessageShowResponse messageShowResponse = new MessageShowResponse();
        messageShowResponse.setId(message.getId());
        messageShowResponse.setText(message.getText());
        messageShowResponse.setTimeStamp(message.getTimeStamp());
        messageShowResponse.setUser(entityToDto(message.getUser()));
        return messageShowResponse;
    }

}
