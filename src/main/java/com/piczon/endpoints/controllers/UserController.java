package com.piczon.endpoints.controllers;

import com.piczon.data.dto.UserUpdate;
import com.piczon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by franc on 9/20/2016.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        CustomDateEditor customDateEditor = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true);
        webDataBinder.registerCustomEditor(Date.class, customDateEditor);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> index(@RequestParam(value = "is_online", required = false) Boolean isOnline) {
        return userService.index(isOnline);
    }

    @RequestMapping("/{username}/profile")
    public String show(@PathVariable("username") String username, Model model, Principal principal) {
        return userService.show(username, principal.getName(), model);
    }

    @RequestMapping("/{username}/profile/image")
    public @ResponseBody FileSystemResource show(@PathVariable("username") String username) {
        return userService.showImage(username);
    }

    @RequestMapping("/current")
    public ResponseEntity<?> currentUser(Principal principal) {
        return userService.currentUser(principal.getName());
    }

    @RequestMapping("/update")
    public String update(Model model, Principal principal) {
        return userService.update(principal.getName(), model);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("userUpdate") @Validated UserUpdate userUpdate, BindingResult result, Model model, Principal principal) {
        return userService.update(principal.getName(), userUpdate, result, model);
    }

    @RequestMapping("/update/image")
    public String manageImage(Model model, Principal principal) {
        return userService.manageImage(principal.getName(), model);
    }

    @RequestMapping(value = "/update/upload_image", method = RequestMethod.POST)
    public String uploadImage(@RequestParam("image") MultipartFile multipartFile, Principal principal) {
        return userService.uploadImage(multipartFile, principal.getName());
    }

    @RequestMapping("/update/remove_image")
    public String removeImage(Principal principal) {
        return userService.removeImage(principal.getName());
    }

}
