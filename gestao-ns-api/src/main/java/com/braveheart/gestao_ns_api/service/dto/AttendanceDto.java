package com.braveheart.gestao_ns_api.service.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO to represent an already created attendance record.
 * This is the information the API returns to the frontend.
 */
public record AttendanceDto(
        Long id,
        LocalDate date,
        UUID userId,
        String userFullName,
        Long postId,
        String postName,
        Long shiftId,
        String shiftName,
        UUID confirmedByUserId,
        String confirmedByUserFullName,
        String observations
) {}
