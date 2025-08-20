package com.braveheart.gestao_ns_api.service.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for creating a single schedule entry.
 * This represents the data sent from the frontend to create one person's shift.
 *
 * @param userId The ID of the user to be scheduled.
 * @param postId The ID of the post where the user will work.
 * @param shiftId The ID of the shift for this schedule entry.
 * @param date The date of the schedule entry.
 */
public record ScheduleCreationDto(
        UUID userId,
        Long postId,
        Long shiftId,
        LocalDate date
) {}
