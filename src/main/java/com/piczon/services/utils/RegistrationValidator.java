package com.piczon.services.utils;

import com.piczon.data.dao.UserDao;
import com.piczon.data.dao.predicates.PredicateBuilder;
import com.piczon.data.dto.UserCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by franc on 9/20/2016.
 */
@Component("registrationValidator")
public class RegistrationValidator implements Validator {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserCreate.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserCreate user = (UserCreate) o;
        PredicateBuilder.Finish userExistsPredicate = PredicateBuilder.USER.builder().where("username", user.getUsername()).finish();
        if (userDao.exists(userExistsPredicate.getResult())) {
            errors.rejectValue("username", "messages.username_exists");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "messages.confirm_not_matched");
        }
    }

}
