package com.braveheart.gestao_ns_api.web.rest;

import com.braveheart.gestao_ns_api.service.ScheduleService;
import com.braveheart.gestao_ns_api.service.dto.ScheduleCreationDto;
import com.braveheart.gestao_ns_api.service.dto.ScheduleDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
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
    @PostMapping("/schedules")
    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    public ResponseEntity<ScheduleDto> createSchedule(@Valid @RequestBody ScheduleCreationDto scheduleCreationDto) throws URISyntaxException {
        log.debug("REST request to save Schedule : {}", scheduleCreationDto);

        ScheduleDto result = scheduleService.createSchedule(scheduleCreationDto);

        return ResponseEntity
                .created(new URI("/api/schedules/" + result.id()))
                .body(result);
    }

    // TODO: Add endpoints to get schedules (e.g., GET /schedules/my-schedule)
}
