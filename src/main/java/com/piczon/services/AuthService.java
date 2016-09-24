package com.piczon.services;

import com.piczon.data.dto.UserCreate;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * Created by franc on 9/20/2016.
 */
public interface AuthService {

    public String index(Model model);

    public String login(boolean error, Model model);

    public String register(UserCreate userCreate, BindingResult result);

}
