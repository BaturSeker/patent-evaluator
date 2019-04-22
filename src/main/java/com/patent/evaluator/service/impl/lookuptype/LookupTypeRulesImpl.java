package com.patent.evaluator.service.impl.lookuptype;


import com.patent.evaluator.constant.ValidationMessages;
import com.patent.evaluator.domain.lookuptype.LookupType;
import com.patent.evaluator.dto.lookuptype.LookupTypeDto;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import com.patent.evaluator.service.api.lookuptype.LookupTypeRules;
import com.patent.evaluator.service.api.lookuptype.LookupTypeService;
import com.patent.evaluator.util.ValidationHelper;
import com.patent.evaluator.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LookupTypeRulesImpl implements LookupTypeRules {

    private LookupTypeService lookupTypeService;

    @Override
    public void save(LookupTypeDto lookupTypeDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(lookupTypeDto.getName())) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_TYPE);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        lookupTypeService.save(lookupTypeDto);
    }

    @Override
    public void update(Integer genericTypeId, LookupTypeDto lookupTypeDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(lookupTypeDto.getName())) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_TYPE);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(genericTypeId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
        lookupTypeService.update(genericTypeId, lookupTypeDto);
    }

    @Override
    public LookupType read(Integer genericTypeId) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(genericTypeId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
        return lookupTypeService.getLookupType(genericTypeId);
    }

    @Override
    public List<LookupType> readAll() {
        return lookupTypeService.readAll();
    }

    @Override
    public Page<LookupTypeDto> getAll(PageableSearchFilterDto pageRequest) {
        return lookupTypeService.getAll(pageRequest);
    }

    @Autowired
    public void setLookupTypeService(LookupTypeService lookupTypeService) {
        this.lookupTypeService = lookupTypeService;
    }
}

