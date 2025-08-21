package com.braveheart.gestao_ns_api.service.dto;

import com.braveheart.gestao_ns_api.model.reports.enums.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public record RescueReportCreationDto(
        Long postId,
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
        Set<Long> equipmentUsedIds,
        Set<Long> assistingEntitiesIds,
        Set<Long> probableCausesIds,
        VictimDto victim,
        List<WitnessDto> witnesses
) {}
