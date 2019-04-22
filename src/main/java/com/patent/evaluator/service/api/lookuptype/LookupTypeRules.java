package com.patent.evaluator.service.api.lookuptype;

import com.patent.evaluator.domain.lookuptype.LookupType;
import com.patent.evaluator.dto.lookuptype.LookupTypeDto;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LookupTypeRules {

    void save(LookupTypeDto lookupTypeDto);

    void update(Integer lookupTypeId, LookupTypeDto lookupTypeDto);

    LookupType read(Integer lookupTypeId);

    List<LookupType> readAll();

    Page<LookupTypeDto> getAll(PageableSearchFilterDto pageRequest);
}
