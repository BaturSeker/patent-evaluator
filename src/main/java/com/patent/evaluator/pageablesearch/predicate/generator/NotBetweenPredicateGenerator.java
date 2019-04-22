package com.patent.evaluator.pageablesearch.predicate.generator;


import com.patent.evaluator.pageablesearch.model.PredicateSourceDto;
import com.patent.evaluator.pageablesearch.model.SearchCriteriaDto;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.Date;

public class NotBetweenPredicateGenerator extends AbstractPredicateGenerator {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public NotBetweenPredicateGenerator(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public Predicate toPredicate(SearchCriteriaDto criteria) {
        PredicateSourceDto predicateSource = getPredicateSourceDto(root, criteria);
        From from = predicateSource.getFrom();
        String key = predicateSource.getKey();

        if (isDateCriteria(from, key)) {
            return builder.between(from.<Date>get(
                    key), (Date) criteria.getValue(), (Date) criteria.getEndValue());
        } else if (isInstantCriteria(from, key)) {
            return builder.between(from.<Instant>get(
                    key), (Instant) criteria.getValue(), (Instant) criteria.getEndValue());
        } else {
            return builder.between(from.<String>get(
                    key), criteria.getValue().toString(), criteria.getEndValue().toString()
            );
        }
    }

}