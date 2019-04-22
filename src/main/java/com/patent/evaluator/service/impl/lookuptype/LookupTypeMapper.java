package com.patent.evaluator.service.impl.lookuptype;

import com.patent.evaluator.config.mapper.GeneralMapStructConfig;
import com.patent.evaluator.domain.lookuptype.LookupType;
import com.patent.evaluator.dto.lookuptype.LookupTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class, uses = LookupValueMapper.class)
public interface LookupTypeMapper {

    LookupTypeMapper INSTANCE = Mappers.getMapper(LookupTypeMapper.class);

    LookupType dtoToEntity(LookupTypeDto lookupTypeDto);

    @Mapping(source = "typeEnum.lookupTypeEnumId", target = "lookupTypeEnumId")
    LookupTypeDto entityToDto(LookupType lookupType);

    List<LookupType> dtoListToEntityList(List<LookupTypeDto> lookupTypeDtos);

    List<LookupTypeDto> entityListToDtoList(List<LookupType> lookupTypes);
}
