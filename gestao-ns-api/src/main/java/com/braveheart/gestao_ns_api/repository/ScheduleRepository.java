package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.Schedule;
import com.braveheart.gestao_ns_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    /**
     * Finds all schedule entries for a given user within a specific date range.
     * The results are ordered by date.
     *
     * @param user The user to find schedules for.
     * @param startDate The start of the date range (inclusive).
     * @param endDate The end of the date range (inclusive).
     * @return A list of schedules.
     */
    List<Schedule> findByUserAndDateBetweenOrderByDateAsc(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("SELECT s FROM Schedule s WHERE s.post.beach.id = :beachId AND s.date BETWEEN :startDate AND :endDate ORDER BY s.date, s.shift.startTime, s.post.name")
    List<Schedule> findSchedulesByBeachAndDateRange(@Param("beachId") Long beachId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Checks if a specific user is scheduled as a coordinator for a specific beach on a given date.
     * The post name 'Coordenador' is hardcoded as per the business rule.
     *
     * @param userId The ID of the user to check.
     * @param beachId The ID of the beach to check.
     * @param date The date to check for the coordinator shift.
     * @return true if the user is the coordinator, false otherwise.
     */
    @Query("SELECT COUNT(s) > 0 FROM Schedule s WHERE s.user.id = :userId AND s.post.beach.id = :beachId AND s.date = :date AND s.post.name = 'Coordenador'")
    boolean isUserCoordinatorForBeachOnDate(@Param("userId") UUID userId, @Param("beachId") Long beachId, @Param("date") LocalDate date);

}
