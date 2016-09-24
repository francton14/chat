package com.piczon.data.dao;

import com.piczon.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by franc on 9/19/2016.
 */
@Transactional
public interface UserDao extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {
}
