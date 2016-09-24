package com.piczon.data.dao.predicates.impl;

import com.piczon.data.dao.predicates.Predicate;
import com.piczon.data.dao.predicates.SearchCriteria;
import com.piczon.data.entities.QMessage;
import com.piczon.data.entities.User;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * Created by franc on 9/19/2016.
 */
public class MessagePredicate implements Predicate {

    @Override
    public BooleanExpression getPredicate(SearchCriteria searchCriteria) {
        QMessage message = QMessage.message;
        String value = searchCriteria.getValue().toString();
        if (searchCriteria.getKey().equals("id")) {
            return message.id.eq(Long.valueOf(value));
        } else if (searchCriteria.getKey().equals("user")) {
            return message.user.eq((User) searchCriteria.getValue());
        }
        return null;
    }

}
