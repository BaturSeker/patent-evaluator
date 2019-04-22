package com.patent.evaluator.config.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {InstantMapper.class, TimestampMapper.class})
public interface GeneralMapStructConfig {

}
