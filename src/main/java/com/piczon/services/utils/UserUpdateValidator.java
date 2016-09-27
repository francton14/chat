package com.piczon.services.utils;

import com.piczon.data.dao.UserDao;
import com.piczon.data.dao.predicates.PredicateBuilder;
import com.piczon.data.dto.UserUpdate;
import com.piczon.data.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by franc on 9/27/2016.
 */
@Component("userUpdateValidator")
public class UserUpdateValidator implements Validator {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserUpdate.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserUpdate userUpdate = (UserUpdate) o;
        if (StringUtils.isNotEmpty(userUpdate.getUsername())) {
            User user = getCurrentUser();
            PredicateBuilder.Finish usernameUpdatePredicate = PredicateBuilder.USER.builder().where("username", userUpdate.getUsername()).finish();
            if (StringUtils.equals(user.getUsername(), userUpdate.getUsername())) {
                errors.rejectValue("username", "messages.username_not_changed");
            }
            if (userDao.exists(usernameUpdatePredicate.getResult())) {
                errors.rejectValue("username", "messages.username_exists");
            }
        }
        if (StringUtils.isNotEmpty(userUpdate.getOldPassword())) {
            User user = getCurrentUser();
            if (!passwordEncoder.matches(userUpdate.getOldPassword(), user.getPassword())) {
                errors.rejectValue("oldPassword", "messages.old_password_not_matched");
            }
            if (StringUtils.isEmpty(userUpdate.getNewPassword())) {
                errors.rejectValue("newPassword", "NotEmpty.userCreate.password");
            }
            if (!StringUtils.equals(userUpdate.getNewPassword(), userUpdate.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "messages.confirm_not_matched");
            }
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PredicateBuilder.Finish userPredicate = PredicateBuilder.USER.builder().where("username", authentication.getName()).finish();
        return userDao.findOne(userPredicate.getResult());
    }

}
