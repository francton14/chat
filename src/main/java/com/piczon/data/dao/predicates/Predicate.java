package com.piczon.data.dao.predicates;

import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * Created by franc on 9/19/2016.
 */
public interface Predicate {

    public BooleanExpression getPredicate(SearchCriteria searchCriteria);

}
