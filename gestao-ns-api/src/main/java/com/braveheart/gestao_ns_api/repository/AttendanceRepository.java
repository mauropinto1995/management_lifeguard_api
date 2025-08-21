package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    /**
     * Finds all attendance records for a given beach on a specific date.
     * It uses a JPQL query to join through the Post entity.
     *
     * @param beachId The ID of the beach.
     * @param date The date to search for.
     * @return A list of attendances for the specified beach and date.
     */
    @Query("SELECT a FROM Attendance a WHERE a.post.beach.id = :beachId AND a.date = :date ORDER BY a.shift.startTime, a.post.name")
    List<Attendance> findByBeachAndDate(@Param("beachId") Long beachId, @Param("date") LocalDate date);

    /**
     * Finds all attendance records for a given beach within a specific month and year.
     *
     * @param beachId The ID of the beach.
     * @param startDate The first day of the month.
     * @param endDate The last day of the month.
     * @return A list of attendances for the specified beach and month.
     */
    @Query("SELECT a FROM Attendance a WHERE a.post.beach.id = :beachId AND a.date BETWEEN :startDate AND :endDate ORDER BY a.date, a.shift.startTime, a.post.name")
    List<Attendance> findByBeachAndMonth(@Param("beachId") Long beachId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}
