package com.piczon.services.impl;

import com.piczon.data.dao.UserDao;
import com.piczon.data.dao.predicates.PredicateBuilder;
import com.piczon.data.dto.UserShow;
import com.piczon.data.dto.UserUpdate;
import com.piczon.data.entities.User;
import com.piczon.security.UpdatableUser;
import com.piczon.services.UserService;
import com.piczon.services.utils.EntityDtoConverter;
import com.piczon.services.utils.UserUpdateValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by franc on 9/19/2016.
 */
@Service("userService")
@Transactional
@PropertySource({"classpath:/lang/messages.properties", "classpath:/storage.properties"})
public class UserServiceImpl implements UserService {

    @Autowired
    private Environment environment;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserUpdateValidator userUpdateValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public String show(String username, String owner, Model model) {
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        if (userDao.exists(userPredicate.getResult())) {
            User user = userDao.findOne(userPredicate.getResult());
            model.addAttribute("user", user);
            model.addAttribute("isOwner", StringUtils.equals(user.getUsername(), owner));
        } else {
            model.addAttribute("error", environment.getProperty("messages.user_not_found"));
        }
        return "profile";
    }

    @Override
    public FileSystemResource showImage(String username) {
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        FileSystemResource fileSystemResource = null;
        if (userDao.exists(userPredicate.getResult())) {
            User user = userDao.findOne(userPredicate.getResult());
            String path = environment.getProperty("file-system.storage.image") + user.getId() + "/" + user.getImageFilename();
            fileSystemResource = new FileSystemResource(path);
        }
        return fileSystemResource;
    }

    @Override
    public ResponseEntity<?> currentUser(String username) {
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        return ResponseEntity.ok(EntityDtoConverter.entityToDto(userDao.findOne(userPredicate.getResult())));
    }

    @Override
    public String update(String username, Model model) {
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        model.addAttribute("user", userDao.findOne(userPredicate.getResult()));
        model.addAttribute("userUpdate", new UserUpdate());
        return "update.profile";
    }

    @Override
    public String update(String username, UserUpdate userUpdate, BindingResult result, Model model) {
        userUpdateValidator.validate(userUpdate, result);
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        User user = userDao.findOne(userPredicate.getResult());
        if (!result.hasErrors()) {
            if (StringUtils.isNotEmpty(userUpdate.getUsername())) {
                user.setUsername(userUpdate.getUsername());
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UpdatableUser userDetails = (UpdatableUser) authentication.getPrincipal();
                userDetails.setUsername(userUpdate.getUsername());
            }
            if (StringUtils.isNotEmpty(userUpdate.getOldPassword())) {
                user.setPassword(passwordEncoder.encode(userUpdate.getNewPassword()));
            }
            if (StringUtils.isNotEmpty(userUpdate.getFirstName())) {
                user.setFirstName(userUpdate.getFirstName());
            }
            if (StringUtils.isNotEmpty(userUpdate.getLastName())) {
                user.setLastName(userUpdate.getLastName());
            }
            if (StringUtils.isNotEmpty(userUpdate.getEmail())) {
                user.setEmail(userUpdate.getEmail());
            }
            if (userUpdate.getBirthday() != null) {
                user.setBirthday(userUpdate.getBirthday());
            }
            if (StringUtils.isNotEmpty(userUpdate.getAboutMe())) {
                user.setAboutMe(userUpdate.getAboutMe());
            }
            userDao.saveAndFlush(user);
            return "redirect:/user/" + user.getUsername() + "/profile";
        }
        model.addAttribute("user", userDao.findOne(userPredicate.getResult()));
        return "update.profile";
    }

    @Override
    public String manageImage(String username, Model model) {
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        model.addAttribute("user", userDao.findOne(userPredicate.getResult()));
        return "update.image";
    }

    @Override
    public String uploadImage(MultipartFile multipartFile, String username) {
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        User user = userDao.findOne(userPredicate.getResult());
        deleteUserImage(user);
        String path = environment.getProperty("file-system.storage.image") + user.getId() + "/" + multipartFile.getOriginalFilename();
        System.out.println("Path: " + path);
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileCopyUtils.copy(multipartFile.getBytes(), new File(path));
            user.setImageFilename(multipartFile.getOriginalFilename());
            userDao.saveAndFlush(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/user/" + user.getUsername() + "/profile";
    }

    @Override
    public String removeImage(String username) {
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", username).finish();
        User user = userDao.findOne(userPredicate.getResult());
        deleteUserImage(user);
        user.setImageFilename(null);
        userDao.saveAndFlush(user);
        return "redirect:/user/" + user.getUsername() + "/profile";
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

    private void deleteUserImage(User user) {
        if (StringUtils.isNotEmpty(user.getImageFilename())) {
            new File(environment.getProperty("file-system.storage.image") + user.getId() + "/" + user.getImageFilename()).delete();
        }
    }

}
