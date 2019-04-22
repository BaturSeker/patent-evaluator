package com.patent.evaluator.pageablesearch.predicate.generator;


import com.patent.evaluator.pageablesearch.model.PredicateSourceDto;
import com.patent.evaluator.pageablesearch.model.SearchCriteriaDto;

import javax.persistence.criteria.*;

public class NotEqualPredicateGenerator extends AbstractPredicateGenerator {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public NotEqualPredicateGenerator(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public Predicate toPredicate(SearchCriteriaDto criteria) {
        PredicateSourceDto predicateSource = getPredicateSourceDto(root, criteria);
        From<?, ?> from = predicateSource.getFrom();

        return builder.notEqual(from.get(predicateSource.getKey()), criteria.getValue());
    }

}