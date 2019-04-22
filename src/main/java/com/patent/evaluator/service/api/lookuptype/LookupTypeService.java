package com.patent.evaluator.service.api.lookuptype;

import com.patent.evaluator.domain.lookuptype.LookupType;
import com.patent.evaluator.dto.lookuptype.LookupTypeDto;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LookupTypeService {

    LookupType save(LookupTypeDto lookupTypeDto);

    LookupType getLookupType(Integer lookupTypeId);

    List<LookupType> readAll();

    Page<LookupTypeDto> getAll(PageableSearchFilterDto filterDto);

    LookupType update(Integer lookupTypeId, LookupTypeDto lookupTypeDto);

}
