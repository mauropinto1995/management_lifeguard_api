package com.braveheart.gestao_ns_api.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ScheduleDto(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String postName,
        UUID userId,
        String userFirstName,
        String userLastName
) {}
