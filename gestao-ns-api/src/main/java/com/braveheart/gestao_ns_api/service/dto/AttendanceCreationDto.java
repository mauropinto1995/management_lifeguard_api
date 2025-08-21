package com.braveheart.gestao_ns_api.service.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for creating a new attendance record.
 * Contains the information sent from the frontend (by the coordinator).
 *
 * @param scheduleId The ID of the original schedule (optional, if the attendance corresponds to the plan).
 * @param userId The ID of the lifeguard who was present.
 * @param postId The ID of the post where the lifeguard was stationed.
 * @param shiftId The ID of the shift the lifeguard worked.
 * @param date The date of the attendance.
 * @param observations Additional observations from the coordinator.
 */
public record AttendanceCreationDto(
        Long scheduleId,
        UUID userId,
        Long postId,
        Long shiftId,
        LocalDate date,
        String observations
) {}
