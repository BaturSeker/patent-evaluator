package com.patent.evaluator.dao;

import com.patent.evaluator.abstracts.BaseRepository;
import com.patent.evaluator.constant.LookupTypeEnum;
import com.patent.evaluator.domain.lookuptype.LookupType;
import com.patent.evaluator.domain.lookuptype.LookupValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookupValueRepository extends BaseRepository<LookupValue, Integer> {
    List<LookupValue> findAllByLookupType(LookupType lookupType);


    List<LookupValue> findAllByLookupTypeTypeEnum(LookupTypeEnum lookupTypeEnum);

    Page<LookupValue> findAllByLookupTypeTypeEnum(LookupTypeEnum lookupTypeEnum, Pageable pageRequest);

    LookupValue findByLookupTypeAndValue(LookupType lookupType, String value);

    @Query(nativeQuery = true, value = "select LookupValueId as VALUE, Value as TEXT from LookupValue WHERE LookupTypeId=?1")
    List<Object[]> getComboLookupValues(Integer lookupTypeId);

    List<LookupValue> findByValue(String value);

    LookupValue getByValue(String Values);
}
