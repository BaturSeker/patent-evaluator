package com.patent.evaluator.service.impl.mapper;

import java.time.Instant;

public class InstantMapper {

    public String asString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }

    public Instant asInstant(String instant) {
        return instant != null ? Instant.parse(instant) : null;
    }
}
