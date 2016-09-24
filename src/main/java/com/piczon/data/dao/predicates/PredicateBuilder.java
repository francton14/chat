package com.piczon.data.dao.predicates;

import com.piczon.data.dao.predicates.impl.MessagePredicate;
import com.piczon.data.dao.predicates.impl.UserPredicate;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * Created by franc on 9/19/2016.
 */
public enum PredicateBuilder {
    USER(new UserPredicate()),
    MESSAGE(new MessagePredicate());

    private Predicate predicate;

    PredicateBuilder(Predicate predicate) {
        this.predicate = predicate;
    }

    public Build builder() {
        return new Builder(predicate);
    }

    public static class Builder implements Conjunction, Build, Finish {

        private Predicate predicate;

        private BooleanExpression result;

        public Builder(Predicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public Conjunction where(String key, Object value) {
            result = predicate.getPredicate(new SearchCriteria(key, value));
            return this;
        }

        @Override
        public Conjunction where(String key, String operation, Object value) {
            result = predicate.getPredicate(new SearchCriteria(key, operation, value));
            return this;
        }

        @Override
        public Conjunction or(String key, Object value) {
            result = result.or(predicate.getPredicate(new SearchCriteria(key, value)));
            return this;
        }

        @Override
        public Conjunction or(String key, String operation, Object value) {
            result = result.or(predicate.getPredicate(new SearchCriteria(key, operation, value)));
            return this;
        }

        @Override
        public Conjunction and(String key, Object value) {
            result = result.and(predicate.getPredicate(new SearchCriteria(key, value)));
            return this;
        }

        @Override
        public Conjunction and(String key, String operation, Object value) {
            result = result.and(predicate.getPredicate(new SearchCriteria(key, operation, value)));
            return this;
        }

        @Override
        public Finish finish() {
            return this;
        }

        @Override
        public BooleanExpression getResult() {
            return result;
        }
    }

    public interface Build {

        public Conjunction where(String key, Object value);

        public Conjunction where(String key, String operation, Object value);

    }

    public interface Conjunction {

        public Conjunction or(String key, Object value);

        public Conjunction or(String key, String operation, Object value);

        public Conjunction and(String key, Object value);

        public Conjunction and(String key, String operation, Object value);

        public Finish finish();

    }

    public interface Finish {

        public BooleanExpression getResult();

    }

}
