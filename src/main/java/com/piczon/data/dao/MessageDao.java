package com.piczon.data.dao;

import com.piczon.data.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by franc on 9/19/2016.
 */
@Transactional
public interface MessageDao extends JpaRepository<Message, Long>, QueryDslPredicateExecutor<Message> {
}
