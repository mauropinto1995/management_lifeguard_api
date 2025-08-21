package com.braveheart.gestao_ns_api.service;

import com.braveheart.gestao_ns_api.service.dto.AttendanceCreationDto;
import com.braveheart.gestao_ns_api.service.dto.AttendanceDto;
import com.braveheart.gestao_ns_api.service.dto.AttendanceUpdateDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    /**
     * Creates a new attendance record.
     * Includes a check to ensure that only the coordinator of the day for the given beach
     * can create the record.
     *
     * @param dto The data for the new attendance record.
     * @return The created attendance record, converted to a DTO.
     */
    AttendanceDto createAttendance(AttendanceCreationDto dto);

    /**
     * Finds all attendance records for a specific beach on a given date.
     * Includes a check to ensure that only an editor or the coordinator of the day
     * for that beach can view the records.
     *
     * @param beachId The ID of the beach.
     * @param date The date to search for.
     * @return A list of attendance records for that beach and date.
     */
    List<AttendanceDto> findAttendancesForBeach(Long beachId, LocalDate date);

    /**
     * Finds all attendance records for a specific beach for a given month and year.
     * Only accessible by editors.
     *
     * @param beachId The ID of the beach.
     * @param year The year to search for.
     * @param month The month to search for (1-12).
     * @return A list of attendance records.
     */
    List<AttendanceDto> findAttendancesForBeachByMonth(Long beachId, int year, int month);

    /**
     * Updates an existing attendance record.
     * Only accessible by editors.
     *
     * @param id The ID of the attendance record to update.
     * @param dto The DTO with the updated data.
     * @return The updated attendance record.
     */
    AttendanceDto updateAttendance(Long id, AttendanceUpdateDto dto);
}
