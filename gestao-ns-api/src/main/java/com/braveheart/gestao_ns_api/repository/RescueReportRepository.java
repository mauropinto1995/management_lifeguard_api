package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.reports.RescueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RescueReportRepository extends JpaRepository<RescueReport,Long> {

    /**
     * Finds all reports created by a specific lifeguard, ordered by the most recent first.
     *
     * @param lifeguardId The UUID of the lifeguard.
     * @return A list of rescue reports.
     */
    List<RescueReport> findByLifeguardIdOrderByOccurrenceTimestampDesc(UUID lifeguardId);

    /**
     * Finds all reports for a given beach within a specific date range.
     * @param beachId The ID of the beach.
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @return A list of rescue reports.
     */
    @Query("SELECT r FROM RescueReport r WHERE r.post.beach.id = :beachId AND r.occurrenceTimestamp >= :startDate AND r.occurrenceTimestamp < :endDate ORDER BY r.occurrenceTimestamp DESC")
    List<RescueReport> findByBeachAndDateRange(
            @Param("beachId") Long beachId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );
}
