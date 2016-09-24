package com.piczon.endpoints.controllers;

import com.piczon.data.dto.UserCreate;
import com.piczon.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by franc on 9/20/2016.
 */
@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping({"/", "/register"})
    public String index(Model model) {
        return authService.index(model);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) boolean error, Model model) {
        return authService.login(error, model);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("userCreate") @Validated UserCreate userCreate, BindingResult result) {
        return authService.register(userCreate, result);
    }

}
