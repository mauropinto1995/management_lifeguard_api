package com.braveheart.gestao_ns_api.web.rest;

import com.braveheart.gestao_ns_api.service.ScheduleService;
import com.braveheart.gestao_ns_api.service.dto.ScheduleCreationDto;
import com.braveheart.gestao_ns_api.service.dto.ScheduleDto;
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
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * POST /schedules : Create a new schedule entry.
     * This endpoint is protected and only accessible by users with the 'EDITOR' role.
     *
     * @param scheduleCreationDto the DTO with the data to create the new schedule entry.
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduleDto,
     * or with status 409 (Conflict) if a schedule conflict occurs.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    public ResponseEntity<ScheduleDto> createSchedule(@Valid @RequestBody ScheduleCreationDto scheduleCreationDto) throws URISyntaxException {
        log.debug("REST request to save Schedule : {}", scheduleCreationDto);

        ScheduleDto result = scheduleService.createSchedule(scheduleCreationDto);

        return ResponseEntity
                .created(new URI("/api/schedules/" + result.id()))
                .body(result);
    }

    /**
     * GET /schedules/my-schedule : Obtém as escalas para o utilizador autenticado num determinado período.
     * Os parâmetros startDate e endDate são opcionais.
     * @param startDate A data de início do período (formato AAAA-MM-DD).
     * @param endDate A data de fim do período (formato AAAA-MM-DD).
     * @return A lista de escalas para o período solicitado.
     */
    @GetMapping("/my-schedule")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ScheduleDto>> getMyScheduleForPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.debug("REST request to get current user's schedules for period: {} to {}", startDate, endDate);
        List<ScheduleDto> schedules = scheduleService.findMySchedules(startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/beach/{beachId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ScheduleDto>> getSchedulesForBeach(
            @PathVariable Long beachId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.debug("REST request to get schedules for beach ID {} for period: {} to {}", beachId, startDate, endDate);
        List<ScheduleDto> schedules = scheduleService.findSchedulesForBeach(beachId, startDate, endDate);
        return ResponseEntity.ok(schedules);
    }
}
