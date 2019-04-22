package com.patent.evaluator.service.impl.lookuptype;


import com.patent.evaluator.constant.LookupTypeEnum;
import com.patent.evaluator.dao.LookupTypeRepository;
import com.patent.evaluator.dao.LookupValueRepository;
import com.patent.evaluator.domain.lookuptype.LookupType;
import com.patent.evaluator.domain.lookuptype.LookupValue;
import com.patent.evaluator.dto.lookuptype.LookupValueDto;
import com.patent.evaluator.service.api.lookuptype.LookupValueService;
import com.patent.evaluator.util.ComboResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LookupValueServiceImpl implements LookupValueService {

    private LookupValueRepository lookupValueRepository;
    private LookupTypeRepository lookupTypeRepository;

    @Override
    public List<LookupValueDto> getAllValuesByLookupTypeEnum(LookupTypeEnum typeEnum) {
        List<LookupValue> values = lookupValueRepository.findAllByLookupTypeTypeEnum(typeEnum);
        return LookupValueMapper.INSTANCE.entityListToDtoList(values);
    }

    @Override
    public Page<LookupValueDto> getValuesByLookupTypeEnum(LookupTypeEnum typeEnum, Pageable pageRequest) {
        Page<LookupValue> values = lookupValueRepository.findAllByLookupTypeTypeEnum(typeEnum, pageRequest);
        return LookupValueMapper.INSTANCE.entityPageToDtoPage(values);
    }

    @Override
    public LookupValue save(LookupValueDto lookupValueDto) {
        LookupValue lookupValue = new LookupValue();
        lookupValue.setValue(lookupValueDto.getValue());
        LookupType lookupType = lookupTypeRepository.getOne(lookupValueDto.getLookupTypeId());
        lookupValue.setLookupType(lookupType);
        return lookupValueRepository.saveAndFlush(lookupValue);
    }

    @Override
    public LookupValue update(Integer lookupValueId, String value) {
        LookupValue lookupValue = this.lookupValueRepository.getOne(lookupValueId);
        lookupValue.setValue(value);
        return this.lookupValueRepository.saveAndFlush(lookupValue);
    }

    @Override
    public LookupValue read(Integer lookupValueId) {
        return this.lookupValueRepository.getOne(lookupValueId);
    }

    @Override
    public List<LookupValue> readAll() {
        return this.lookupValueRepository.findAll();
    }

    @Override
    public List<LookupValue> readAllByLookupTypeId(Integer lookupTypeId) {
        LookupType lookupType = lookupTypeRepository.getOne(lookupTypeId);
        return this.lookupValueRepository.findAllByLookupType(lookupType);
    }

    @Override
    public List<LookupValue> readAllIcon() {
        LookupType lookupType = lookupTypeRepository.findByName("Icon");
        return lookupValueRepository.findAllByLookupType(lookupType);
    }

    @Override
    public List getComboLookupValues(Integer lookupTypeId) {
        List<Object[]> resultList = this.lookupValueRepository.getComboLookupValues(lookupTypeId);
        return ComboResponseBuilder.buildComboResponseList(resultList);
    }

    @Override
    public void changeActive(Integer lookupValueId) {
        LookupValue lookupValue = this.lookupValueRepository.getOne(lookupValueId);
        Boolean active = lookupValue.getActive();
        lookupValue.setActive(!active);
        this.lookupValueRepository.save(lookupValue);
    }

    @Autowired
    public void setLookupValueRepository(LookupValueRepository lookupValueRepository) {
        this.lookupValueRepository = lookupValueRepository;
    }

    @Autowired
    public void setLookupTypeRepository(LookupTypeRepository lookupTypeRepository) {
        this.lookupTypeRepository = lookupTypeRepository;
    }
}

