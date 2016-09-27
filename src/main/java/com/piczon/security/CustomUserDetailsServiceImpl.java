package com.piczon.security;

import com.piczon.data.dao.UserDao;
import com.piczon.data.dao.predicates.PredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franc on 9/19/2016.
 */
@Service("customUserDetailsService")
@Transactional
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return buildUser(userDao.findOne(PredicateBuilder.USER.builder().where("username", s).finish().getResult()));
    }

    private User buildUser(com.piczon.data.entities.User user) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UpdatableUser(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

}
