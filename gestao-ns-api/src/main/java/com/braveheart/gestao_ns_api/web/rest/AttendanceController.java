package com.braveheart.gestao_ns_api.web.rest;

import com.braveheart.gestao_ns_api.service.AttendanceService;
import com.braveheart.gestao_ns_api.service.dto.AttendanceCreationDto;
import com.braveheart.gestao_ns_api.service.dto.AttendanceDto;
import com.braveheart.gestao_ns_api.service.dto.AttendanceUpdateDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final Logger log = LoggerFactory.getLogger(AttendanceController.class);

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * POST /attendances : Creates a new attendance record.
     * Authorization is handled within the service to ensure that only the coordinator
     * of the day for the given beach can create the record.
     *
     * @param dto the data for the new attendance record.
     * @return the ResponseEntity with status 201 (Created) and the new DTO in the body.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttendanceDto> createAttendance(@Valid @RequestBody AttendanceCreationDto dto) throws URISyntaxException {
        log.debug("REST request to create Attendance: {}", dto);
        AttendanceDto result = attendanceService.createAttendance(dto);
        return ResponseEntity
                .created(new URI("/api/attendances/" + result.id()))
                .body(result);
    }

    /**
     * GET /attendances/beach/{beachId} : Gets all attendance records for a specific beach on a given date.
     * Authorization is handled within the service.
     *
     * @param beachId The ID of the beach.
     * @param date The date to search for (optional, defaults to today).
     * @return A list of attendance records.
     */
    @GetMapping("/beach/{beachId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AttendanceDto>> getAttendancesForBeach(
            @PathVariable Long beachId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.debug("REST request to get attendances for beach ID {} on date {}", beachId, date);
        List<AttendanceDto> attendances = attendanceService.findAttendancesForBeach(beachId, date);
        return ResponseEntity.ok(attendances);
    }

    /**
     * GET /attendances/beach/{beachId}/monthly : Gets all attendance records for a specific beach for a given month.
     * Only accessible by editors.
     *
     * @param beachId The ID of the beach.
     * @param year The year of the month to search for.
     * @param month The month to search for (1-12).
     * @return A list of attendance records.
     */
    @GetMapping("/beach/{beachId}/monthly")
    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    public ResponseEntity<List<AttendanceDto>> getMonthlyAttendancesForBeach(
            @PathVariable Long beachId,
            @RequestParam int year,
            @RequestParam int month) {
        log.debug("REST request to get monthly attendances for beach ID {} for {}/{}", beachId, year, month);
        List<AttendanceDto> attendances = attendanceService.findAttendancesForBeachByMonth(beachId, year, month);
        return ResponseEntity.ok(attendances);
    }


    /**
     * PUT /attendances/{id} : Updates an existing attendance record.
     * Only accessible by editors.
     *
     * @param id The ID of the attendance record to update.
     * @param dto The DTO with the updated data.
     * @return The updated attendance record.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    public ResponseEntity<AttendanceDto> updateAttendance(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceUpdateDto dto) {
        log.debug("REST request to update Attendance with ID {}: {}", id, dto);
        AttendanceDto result = attendanceService.updateAttendance(id, dto);
        return ResponseEntity.ok(result);
    }
}
