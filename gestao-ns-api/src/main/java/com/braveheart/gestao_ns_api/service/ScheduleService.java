package com.braveheart.gestao_ns_api.service;

import com.braveheart.gestao_ns_api.service.dto.ScheduleCreationDto;
import com.braveheart.gestao_ns_api.service.dto.ScheduleDto;

public interface ScheduleService {

    /**
     * Creates a new schedule entry.
     *
     * @param dto The DTO containing the data for the new schedule entry.
     * @return The created Schedule entry, converted to a DTO.
     */
    ScheduleDto createSchedule(ScheduleCreationDto dto);

    // TODO: Add methods for finding schedules, e.g., findSchedulesForUser, findSchedulesForBeach, etc.
}
