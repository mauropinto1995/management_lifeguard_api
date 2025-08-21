package com.braveheart.gestao_ns_api.service.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for updating an existing attendance record.
 * Contains the data that an editor can modify.
 */
public record AttendanceUpdateDto(
        UUID userId,
        Long postId,
        Long shiftId,
        LocalDate date,
        String observations
) {}