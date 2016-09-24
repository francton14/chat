package com.piczon.data.dao.predicates.impl;

import com.piczon.data.dao.predicates.Predicate;
import com.piczon.data.dao.predicates.SearchCriteria;
import com.piczon.data.entities.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by franc on 9/19/2016.
 */
public class UserPredicate implements Predicate {

    @Override
    public BooleanExpression getPredicate(SearchCriteria searchCriteria) {
        QUser user = QUser.user;
        String value = searchCriteria.getValue().toString();
        if (searchCriteria.getKey().equals("username")) {
            if (StringUtils.isEmpty(searchCriteria.getOperation()) || StringUtils.equals(searchCriteria.getOperation(), "username")) {
                return user.username.eq(value);
            } else if (searchCriteria.getOperation().equals("in")) {
                return user.username.in((List<String>) searchCriteria.getValue());
            } else if (searchCriteria.getOperation().equals("notIn")) {
                return user.username.notIn((List<String>) searchCriteria.getValue());
            }
        }
        return null;
    }

}
