package com.patent.evaluator.dao;

import com.patent.evaluator.abstracts.BaseRepository;
import com.patent.evaluator.domain.lookuptype.LookupType;
import org.springframework.stereotype.Repository;

@Repository
public interface LookupTypeRepository extends BaseRepository<LookupType, Integer> {
    LookupType findByName(String name);
}

