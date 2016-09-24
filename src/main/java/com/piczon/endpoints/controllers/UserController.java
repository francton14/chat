package com.piczon.endpoints.controllers;

import com.piczon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by franc on 9/20/2016.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> index(@RequestParam(value = "is_online", required = false) Boolean isOnline) {
        return userService.index(isOnline);
    }

    @RequestMapping("/show/{userId}")
    public ResponseEntity<?> show(@PathVariable("userId") Long userId) {
        return userService.show(userId);
    }

}
