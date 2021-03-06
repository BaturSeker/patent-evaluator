package com.patent.evaluator.service.impl.lookuptype;


import com.patent.evaluator.constant.ValidationMessages;
import com.patent.evaluator.domain.lookuptype.LookupValue;
import com.patent.evaluator.dto.lookuptype.LookupValueDto;
import com.patent.evaluator.service.api.lookuptype.LookupValueRules;
import com.patent.evaluator.service.api.lookuptype.LookupValueService;
import com.patent.evaluator.util.ValidationHelper;
import com.patent.evaluator.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LookupValueRulesImpl implements LookupValueRules {
    private LookupValueService lookupValueService;

    @Override
    public void save(LookupValueDto lookupValueDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notNull(lookupValueDto)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(lookupValueDto.getValue())) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_DESCRIPTION);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(lookupValueDto.getLookupTypeId())) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_LOOKUP_TYPE_ID_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        lookupValueService.save(lookupValueDto);
    }

    @Override
    public void update(Integer genericTypeValueId, String value) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(value)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_NAME);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(genericTypeValueId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        lookupValueService.update(genericTypeValueId, value);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LookupValue read(Integer genericTypeValueId) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(genericTypeValueId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
        return lookupValueService.read(genericTypeValueId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List getComboLookupValues(Integer lookupTypeValueId) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(lookupTypeValueId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
        return lookupValueService.getComboLookupValues(lookupTypeValueId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValue> readAll() {
        return lookupValueService.readAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValue> readAllByLookupTypeId(Integer genericTypeId) {
        return lookupValueService.readAllByLookupTypeId(genericTypeId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValue> readAllIcons() {
        return lookupValueService.readAllIcon();
    }

    @Override
    public void changeActive(Integer genericTypeValueId) {
        lookupValueService.changeActive(genericTypeValueId);
    }

    @Autowired
    public void setLookupValueService(LookupValueService lookupValueService) {
        this.lookupValueService = lookupValueService;
    }
}

