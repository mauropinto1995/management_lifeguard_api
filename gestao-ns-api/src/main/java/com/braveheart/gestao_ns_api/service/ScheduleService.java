package com.braveheart.gestao_ns_api.service;

import com.braveheart.gestao_ns_api.service.dto.ScheduleCreationDto;
import com.braveheart.gestao_ns_api.service.dto.ScheduleDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    /**
     * Creates a new schedule entry.
     *
     * @param dto The DTO containing the data for the new schedule entry.
     * @return The created Schedule entry, converted to a DTO.
     */
    ScheduleDto createSchedule(ScheduleCreationDto dto);

    /**
     * Finds all upcoming schedules for the currently authenticated user,
     * respecting the global visibility settings.
     *
     * @return A list of the current user's upcoming schedules.
     */
    List<ScheduleDto> findMySchedules(LocalDate startDate, LocalDate endDate);

    List<ScheduleDto> findSchedulesForBeach(Long beachId, LocalDate startDate, LocalDate endDate);

    // TODO: Add methods for finding schedules, e.g., findSchedulesForUser, findSchedulesForBeach, etc.
}
