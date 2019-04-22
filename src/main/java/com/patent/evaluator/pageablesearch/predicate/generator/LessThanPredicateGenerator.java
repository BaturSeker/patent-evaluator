package com.patent.evaluator.pageablesearch.predicate.generator;

import com.patent.evaluator.pageablesearch.model.PredicateSourceDto;
import com.patent.evaluator.pageablesearch.model.SearchCriteriaDto;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.Date;

public class LessThanPredicateGenerator extends AbstractPredicateGenerator {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public LessThanPredicateGenerator(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public Predicate toPredicate(SearchCriteriaDto criteria) {
        PredicateSourceDto predicateSource = getPredicateSourceDto(root, criteria);
        String key = predicateSource.getKey();
        From<?, ?> from = predicateSource.getFrom();

        if (isDateCriteria(from, criteria.getKey())) {
            return builder.lessThan(from.<Date>get(
                    key), (Date) criteria.getValue());
        } else if (isInstantCriteria(from, criteria.getKey())) {
            return builder.lessThan(from.<Instant>get(
                    key), (Instant) criteria.getValue());
        } else {
            return builder.lessThan(from.<String>get(
                    key), criteria.getValue().toString());
        }
    }

}