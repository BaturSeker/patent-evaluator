package com.patent.evaluator.service.impl.role;

import com.patent.evaluator.config.mapper.GeneralMapStructConfig;
import com.patent.evaluator.domain.Roles;
import com.patent.evaluator.dto.role.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Roles dtoToEntity(RoleDto dto);

    RoleDto entityToDto(Roles entity);

    List<Roles> dtoListToEntityList(List<RoleDto> dtoList);

    List<RoleDto> entityListToDtoList(List<Roles> entityList);
}

