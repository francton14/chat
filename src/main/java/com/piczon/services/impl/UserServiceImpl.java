package com.piczon.services.impl;

import com.piczon.data.dao.UserDao;
import com.piczon.data.dao.predicates.PredicateBuilder;
import com.piczon.data.dto.UserShow;
import com.piczon.data.entities.User;
import com.piczon.services.UserService;
import com.piczon.services.utils.EntityDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franc on 9/19/2016.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public ResponseEntity<?> index(Boolean online) {
        Iterable<User> userEntities;
        List<UserShow> users = new ArrayList<>();
        if (online != null) {
            List<String> onlineUsers = getOnlineUsers();
            PredicateBuilder.Finish builder;
            if (online) {
                builder = PredicateBuilder.USER.builder().where("username", "in", onlineUsers).finish();
            } else {
                builder = PredicateBuilder.USER.builder().where("username", "notIn", onlineUsers).finish();
            }
            userEntities = userDao.findAll(builder.getResult());
        } else {
            userEntities = userDao.findAll();
        }
        userEntities.forEach(user -> {
            users.add(EntityDtoConverter.entityToDto(user));
        });
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<?> show(Long userId) {
        if (userDao.exists(userId)) {
            return ResponseEntity.ok(EntityDtoConverter.entityToDto(userDao.findOne(userId)));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> currentUser(String username) {
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        return ResponseEntity.ok(EntityDtoConverter.entityToDto(userDao.findOne(userPredicate.getResult())));
    }

    private List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        sessionRegistry.getAllPrincipals().forEach(principal -> {
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
                onlineUsers.add(user.getUsername());
            }
        });
        return onlineUsers;
    }

}
