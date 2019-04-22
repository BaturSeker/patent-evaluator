package com.patent.evaluator.pageablesearch.specification;


import com.patent.evaluator.abstracts.BaseEntityWithoutId;
import com.patent.evaluator.pageablesearch.criteria.CriteriaKeyFieldTypeDeterminerUtil;
import com.patent.evaluator.pageablesearch.exception.SearchKeyNotAllowedException;
import com.patent.evaluator.pageablesearch.exception.SearchOperationNotFoundException;
import com.patent.evaluator.pageablesearch.model.SearchCriteriaDto;
import com.patent.evaluator.pageablesearch.model.SearchOperationEnum;
import com.patent.evaluator.pageablesearch.util.DateParser;
import com.patent.evaluator.pageablesearch.util.IsoDateParser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Build Specification object to pass repository to find suitable entities
 *
 * @param <T> entity class
 */
public class SearchSpecificationBuilder<T> {

    public static final String DELETED_ON_KEY = "deletedOn";

    private DateParser dateParser = new IsoDateParser();

    /**
     * Do not allow to filter by unspecified fields of the entity for security reasons
     */
    private Set<String> allowedFilterKeys = new HashSet<>();
    private boolean allowAllKeys = false;
    private Class<T> typeOfEntity;

    /**
     * Allow all fields of entity to be filtered
     *
     * @param typeOfEntity
     * @return
     */
    public static SearchSpecificationBuilder filterAllKeysInstance(Class typeOfEntity) {
        Set<String> allowedFilterKeys = new HashSet<>();
        ReflectionUtils.doWithFields(typeOfEntity, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                allowedFilterKeys.add(field.getName());
            }
        });

        return new SearchSpecificationBuilder(typeOfEntity, allowedFilterKeys, true);
    }

    /**
     * Filter by only keys excluding passed filter keys
     *
     * @param typeOfEntity
     * @param excludeFilterKeys
     * @return
     */
    public static SearchSpecificationBuilder filterExcludingKeysInstance(Class typeOfEntity, String... excludeFilterKeys) {
        Set<Field> declaredFields = new HashSet<>();
        ReflectionUtils.doWithFields(typeOfEntity, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                declaredFields.add(field);
            }
        });

        Set<String> excludeList = Stream.of(excludeFilterKeys).collect(Collectors.toSet());

        Set<String> allowedFilterKeys = new HashSet<>();
        for (Field field : declaredFields) {
            if (!excludeList.contains(field.getName())) {
                allowedFilterKeys.add(field.getName());
            }
        }

        return new SearchSpecificationBuilder(typeOfEntity, allowedFilterKeys);
    }

    /**
     * Filter by only allowed filter keys
     *
     * @param typeOfEntity
     * @param allowedFilterKeys allowed filter keys
     * @return
     */
    public static SearchSpecificationBuilder filterAllowedKeysInstance(Class typeOfEntity, String... allowedFilterKeys) {
        return new SearchSpecificationBuilder(typeOfEntity, allowedFilterKeys);
    }

    protected SearchSpecificationBuilder(Class<T> typeOfEntity, String... allowedFilterKeys) {
        this.typeOfEntity = typeOfEntity;
        for (String allowedKey : allowedFilterKeys) {
            this.allowedFilterKeys.add(allowedKey);
        }
    }

    protected SearchSpecificationBuilder(Class<T> typeOfEntity, Set<String> allowedFilterKeys, boolean allowAllKeys) {
        this.typeOfEntity = typeOfEntity;
        this.allowAllKeys = allowAllKeys;
        for (String allowedKey : allowedFilterKeys) {
            this.allowedFilterKeys.add(allowedKey);
        }
    }

    protected SearchSpecificationBuilder(Class<T> typeOfEntity, Set<String> allowedFilterKeys) {
        this.typeOfEntity = typeOfEntity;
        for (String allowedKey : allowedFilterKeys) {
            this.allowedFilterKeys.add(allowedKey);
        }
    }

    public Specification<T> build(List<SearchCriteriaDto> criteriaList) {

        if (criteriaList.isEmpty()) {
            return null;
        }

        if (BaseEntityWithoutId.class.isAssignableFrom(typeOfEntity)) {
            addDeletedOnNullCriteria(criteriaList);
        }

        CriteriaKeyFieldTypeDeterminerUtil.setFieldTypes(criteriaList, typeOfEntity, dateParser);

        List<CustomSearchSpecification<T>> specificationList = createSpecificationList(criteriaList);
        Specification<T> builtSpecification = specificationList.get(0);

        for (int i = 1; i < specificationList.size(); i++) {
            CustomSearchSpecification<T> searchSpecification = specificationList.get(i);
            if (searchSpecification.isOr()) {
                builtSpecification = builtSpecification.or(searchSpecification);
            } else {
                builtSpecification = builtSpecification.and(searchSpecification);
            }
        }

        return builtSpecification;
    }

    private void addDeletedOnNullCriteria(List<SearchCriteriaDto> criteriaList) {
        for (SearchCriteriaDto existingCriteria : criteriaList) {
            if (existingCriteria.getKey().equals(DELETED_ON_KEY)) {
                return;
            }
        }

        SearchCriteriaDto criteria = new SearchCriteriaDto();
        criteria.setKey(DELETED_ON_KEY);
        criteria.setOperation(":");
        criteria.setValue(null);

        criteriaList.add(criteria);
        allowedFilterKeys.add(DELETED_ON_KEY);
    }

    private List<CustomSearchSpecification<T>> createSpecificationList(List<SearchCriteriaDto> criteriaList) {
        List<CustomSearchSpecification<T>> specificationList = new ArrayList<>();

        for (SearchCriteriaDto criteria : criteriaList) {
            validate(criteria);
            specificationList.add(new CustomSearchSpecification(criteria));
        }
        return specificationList;
    }

    private void validate(SearchCriteriaDto criteria) {
        if (!this.allowAllKeys && !allowedFilterKeys.contains(criteria.getKey())) {
            throw new SearchKeyNotAllowedException(criteria.getKey());
        }

        if (SearchOperationEnum.getOperation(criteria.getOperation()) == null) {
            throw new SearchOperationNotFoundException(criteria.getOperation());
        }
    }

    public void setDateParser(DateParser dateParser) {
        this.dateParser = dateParser;
    }
}
