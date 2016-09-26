package com.piczon.services;

import org.springframework.http.ResponseEntity;

/**
 * Created by franc on 9/19/2016.
 */
public interface UserService {

    public ResponseEntity<?> index(Boolean online);

    public ResponseEntity<?> show(Long userId);

    public ResponseEntity<?> currentUser(String username);

}
