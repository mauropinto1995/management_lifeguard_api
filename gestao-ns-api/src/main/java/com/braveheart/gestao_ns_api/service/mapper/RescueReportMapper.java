package com.braveheart.gestao_ns_api.service.mapper;

import com.braveheart.gestao_ns_api.model.reports.RescueReport;
import com.braveheart.gestao_ns_api.model.reports.Victim;
import com.braveheart.gestao_ns_api.model.reports.Witness;
import com.braveheart.gestao_ns_api.service.dto.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RescueReportMapper {

    public RescueReportDto toDto(RescueReport report) {
        if (report == null) {
            return null;
        }

        return new RescueReportDto(
                report.getId(),
                report.getLifeguard().getId(),
                report.getLifeguard().getFirstName() + " " + report.getLifeguard().getLastName(),
                report.getPost().getId(),
                report.getPost().getName(),
                report.getOccurrenceTimestamp(),
                report.isOnDuty(),
                report.getIncidentType(),
                report.getIncidentTypeOther(),
                report.getOutcome(),
                report.getVictimActivity(),
                report.getVictimActivityOther(),
                report.getWind(),
                report.getVisibility(),
                report.getCurrent(),
                report.getTide(),
                report.getWaves(),
                report.getFlag(),
                report.getEvacuation(),
                report.getIncidentEndTime(),
                report.getAdditionalObservations(),
                report.getEquipmentUsed().stream().map(e -> new EquipmentDto(e.getId(), e.getName())).collect(Collectors.toSet()),
                report.getAssistingEntities().stream().map(e -> new AssistingEntityDto(e.getId(), e.getName())).collect(Collectors.toSet()),
                report.getProbableCauses().stream().map(c -> new ProbableCauseDto(c.getId(), c.getName())).collect(Collectors.toSet()),
                toVictimDto(report.getVictim()),
                report.getWitnesses().stream().map(this::toWitnessDto).collect(Collectors.toList()),
                report.getCreatedAt()
        );
    }

    private VictimDto toVictimDto(Victim victim) {
        if (victim == null) return null;
        return new VictimDto(victim.getName(), victim.getNationality(), victim.getAddress(), victim.getPostalCode(), victim.getAge(), victim.getGender());
    }

    private WitnessDto toWitnessDto(Witness witness) {
        if (witness == null) return null;
        return new WitnessDto(witness.getName(), witness.getNationality(), witness.getAddress(), witness.getPostalCode(), witness.getPhoneNumber(), witness.getAge(), witness.getGender(), witness.getSignature());
    }


}
