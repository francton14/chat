package com.piczon.services;

import com.piczon.data.dto.MessageCreateRequest;
import com.piczon.data.dto.MessageDeleteResponse;
import com.piczon.data.dto.MessageShowResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

/**
 * Created by franc on 9/19/2016.
 */
public interface ChatService {

    public String chat(String username, Model model);

    public ResponseEntity<?> index();

    public MessageShowResponse create(MessageCreateRequest request, String username);

    public MessageDeleteResponse delete(Long messageId, String username);

}
