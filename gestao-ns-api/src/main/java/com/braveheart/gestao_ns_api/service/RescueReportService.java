package com.braveheart.gestao_ns_api.service;

import com.braveheart.gestao_ns_api.service.dto.RescueReportCreationDto;
import com.braveheart.gestao_ns_api.service.dto.RescueReportDto;

import java.time.LocalDate;
import java.util.List;

public interface RescueReportService {

    /**
     * Creates a new rescue report.
     *
     * @param dto The DTO containing all the data for the new report.
     * @return The created report, converted to a DTO.
     */
    RescueReportDto createReport(RescueReportCreationDto dto);

    /**
     * Finds all reports submitted by the currently authenticated user.
     *
     * @return A list of the user's rescue reports.
     */
    List<RescueReportDto> findMyReports();

    /**
     * Finds all reports for a specific beach within a given date range.
     * Authorization is handled inside: only editors or the coordinator of the day can access it.
     *
     * @param beachId The ID of the beach.
     * @param startDate The start date of the period.
     * @param endDate The end date of the period.
     * @return A list of rescue reports.
     */
    List<RescueReportDto> findReportsForBeach(Long beachId, LocalDate startDate, LocalDate endDate);

    /**
     * Deletes a rescue report.
     * Authorization is handled inside: only editors or the coordinator of the day/beach can delete it.
     *
     * @param reportId The ID of the report to delete.
     */
    void deleteReport(Long reportId);
}
