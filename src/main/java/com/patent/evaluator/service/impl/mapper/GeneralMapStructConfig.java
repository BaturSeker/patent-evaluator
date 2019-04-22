package com.patent.evaluator.service.impl.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {InstantMapper.class, TimestampMapper.class})
public interface GeneralMapStructConfig {

}
