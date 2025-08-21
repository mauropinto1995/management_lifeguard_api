package com.braveheart.gestao_ns_api.service.dto;

import com.braveheart.gestao_ns_api.model.reports.enums.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * DTO to represent a full rescue report for sending to the frontend.
 */
public record RescueReportDto(
        Long id,
        UUID lifeguardId,
        String lifeguardFullName,
        Long postId,
        String postName,
        Instant occurrenceTimestamp,
        boolean isOnDuty,
        IncidentType incidentType,
        String incidentTypeOther,
        InterventionOutcome outcome,
        VictimActivity victimActivity,
        String victimActivityOther,
        WindCondition wind,
        VisibilityCondition visibility,
        CurrentCondition current,
        TideCondition tide,
        WaveCondition waves,
        FlagCondition flag,
        EvacuationMethod evacuation,
        Instant incidentEndTime,
        String additionalObservations,
        Set<EquipmentDto> equipmentUsed,
        Set<AssistingEntityDto> assistingEntities,
        Set<ProbableCauseDto> probableCauses,
        VictimDto victim,
        List<WitnessDto> witnesses,
        Instant createdAt
) {}
