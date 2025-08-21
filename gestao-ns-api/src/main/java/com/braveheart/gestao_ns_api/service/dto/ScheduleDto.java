package com.braveheart.gestao_ns_api.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ScheduleDto(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String postName,
        UUID userId,
        String userFirstName,
        String userLastName,
        String status // e.g., "VISIBLE", "NOT_PUBLISHED"
) {
    /**
     * Método de fábrica para criar um marcador de posição para um dia não publicado.
     * @param date A data do dia não publicado.
     * @return Um ScheduleDto que representa um dia bloqueado.
     */
    public static ScheduleDto createNotPublished(LocalDate date) {
        return new ScheduleDto(null, date, null, null, null, null, null, null, "NOT_PUBLISHED");
    }
}
