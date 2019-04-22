package com.patent.evaluator.service.impl.lookuptype;

import com.patent.evaluator.config.mapper.GeneralMapStructConfig;
import com.patent.evaluator.domain.lookuptype.LookupValue;
import com.patent.evaluator.dto.lookuptype.LookupValueDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface LookupValueMapper {

    LookupValueMapper INSTANCE = Mappers.getMapper(LookupValueMapper.class);

    LookupValue dtoToEntity(LookupValueDto lookupValueDto);

    @Mapping(source = "lookupType.lookupTypeId", target = "lookupTypeId")
    LookupValueDto entityToDto(LookupValue lookupValue);

    List<LookupValue> dtoListToEntityList(List<LookupValueDto> lookupValueDtos);

    List<LookupValueDto> entityListToDtoList(List<LookupValue> lookupValues);

    default Page<LookupValueDto> entityPageToDtoPage(Page<LookupValue> lookupValues) {
        return lookupValues.map(this::entityToDto);
    }
}