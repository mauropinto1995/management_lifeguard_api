package com.braveheart.gestao_ns_api.web.rest;

import com.braveheart.gestao_ns_api.service.RescueReportService;
import com.braveheart.gestao_ns_api.service.dto.RescueReportCreationDto;
import com.braveheart.gestao_ns_api.service.dto.RescueReportDto;
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
@RequestMapping("/api/rescue-reports")
public class RescueReportController {

    private final Logger log = LoggerFactory.getLogger(RescueReportController.class);

    private final RescueReportService rescueReportService;

    public RescueReportController(RescueReportService rescueReportService) {
        this.rescueReportService = rescueReportService;
    }

    /**
     * POST /rescue-reports : Creates a new rescue report.
     * Accessible by any authenticated user.
     *
     * @param dto the data for the new rescue report.
     * @return the ResponseEntity with status 201 (Created) and the new DTO in the body.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RescueReportDto> createRescueReport(@Valid @RequestBody RescueReportCreationDto dto) throws URISyntaxException {
        log.debug("REST request to create Rescue Report: {}", dto);
        RescueReportDto result = rescueReportService.createReport(dto);
        return ResponseEntity
                .created(new URI("/api/rescue-reports/" + result.id()))
                .body(result);
    }

    /**
     * GET /rescue-reports/my-reports : Gets all reports submitted by the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reports in the body.
     */
    @GetMapping("/my-reports")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RescueReportDto>> getMyReports() {
        log.debug("REST request to get all reports for current user");
        List<RescueReportDto> myReports = rescueReportService.findMyReports();
        return ResponseEntity.ok(myReports);
    }

    /**
     * GET /rescue-reports/beach/{beachId} : Gets all reports for a specific beach in a given period.
     * Authorization is handled inside the service.
     *
     * @param beachId The ID of the beach.
     * @param startDate The start date of the period (optional, defaults to today).
     * @param endDate The end date of the period (optional, defaults to today).
     * @return A list of rescue reports.
     */
    @GetMapping("/beach/{beachId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RescueReportDto>> getReportsForBeach(
            @PathVariable Long beachId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.debug("REST request to get reports for beach ID {} for period {} to {}", beachId, startDate, endDate);
        List<RescueReportDto> reports = rescueReportService.findReportsForBeach(beachId, startDate, endDate);
        return ResponseEntity.ok(reports);
    }

    /**
     * DELETE /rescue-reports/{id} : Deletes a rescue report.
     * Authorization is handled inside the service.
     *
     * @param id The ID of the report to delete.
     * @return the ResponseEntity with status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteRescueReport(@PathVariable Long id) {
        log.debug("REST request to delete Rescue Report with ID: {}", id);
        rescueReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
