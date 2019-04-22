package com.patent.evaluator.service.impl;

import com.patent.evaluator.config.mapper.GeneralMapStructConfig;
import com.patent.evaluator.domain.Authority;
import com.patent.evaluator.dto.AuthorityResponse;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface AuthorityMapper {

    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

    @InheritInverseConfiguration
    Authority dtoToEntity(AuthorityResponse authorityResponse);

    @Mappings({
            @Mapping(source = "id", target = "authorityId"),
    })
    AuthorityResponse entityToDto(Authority authority);

    List<Authority> dtoListToEntityList(List<AuthorityResponse> authorityResponses);

    List<AuthorityResponse> entityListToDtoList(List<Authority> authorities);
}