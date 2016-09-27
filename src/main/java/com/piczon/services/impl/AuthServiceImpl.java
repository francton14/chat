package com.piczon.services.impl;

import com.piczon.data.dao.UserDao;
import com.piczon.data.dto.UserCreate;
import com.piczon.data.entities.User;
import com.piczon.services.AuthService;
import com.piczon.services.utils.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * Created by franc on 9/20/2016.
 */
@Service("authService")
@Transactional
@PropertySource({"classpath:/lang/messages.properties"})
public class AuthServiceImpl implements AuthService {

    @Autowired
    private Environment environment;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RegistrationValidator registrationValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String index(Model model) {
        if (!isAuthenticated()) {
            return "redirect:/chat";
        }
        model.addAttribute("userCreate", new UserCreate());
        return "index";
    }

    @Override
    public String login(boolean error, Model model) {
        if (!isAuthenticated()) {
            return "redirect:/chat";
        }
        if (error) {
            model.addAttribute("error", environment.getProperty("messages.login_error"));
        }
        return "login";
    }

    @Override
    public String register(UserCreate userCreate, BindingResult result) {
        registrationValidator.validate(userCreate, result);
        if (!result.hasErrors()) {
            User user = new User();
            user.setUsername(userCreate.getUsername());
            user.setPassword(passwordEncoder.encode(userCreate.getPassword()));
            user.setFirstName(userCreate.getFirstName());
            user.setLastName(userCreate.getLastName());
            user.setEmail(userCreate.getEmail());
            user.setBirthday(userCreate.getBirthday());
            userDao.saveAndFlush(user);
        }
        return "index";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken;
    }

}
