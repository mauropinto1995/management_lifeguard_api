package com.braveheart.gestao_ns_api.service.impl;

import com.braveheart.gestao_ns_api.exception.ForbiddenException;
import com.braveheart.gestao_ns_api.model.reports.RescueReport;
import com.braveheart.gestao_ns_api.model.reports.Victim;
import com.braveheart.gestao_ns_api.model.reports.Witness;
import org.springframework.stereotype.Service;

import com.braveheart.gestao_ns_api.exception.ResourceNotFoundException;
import com.braveheart.gestao_ns_api.model.*;
import com.braveheart.gestao_ns_api.repository.*;
import com.braveheart.gestao_ns_api.security.SecurityUtils;
import com.braveheart.gestao_ns_api.service.RescueReportService;
import com.braveheart.gestao_ns_api.service.dto.RescueReportCreationDto;
import com.braveheart.gestao_ns_api.service.dto.RescueReportDto;
import com.braveheart.gestao_ns_api.service.dto.VictimDto;
import com.braveheart.gestao_ns_api.service.dto.WitnessDto;
import com.braveheart.gestao_ns_api.service.mapper.RescueReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RescueReportServiceImpl implements RescueReportService {

    private final Logger log = LoggerFactory.getLogger(RescueReportServiceImpl.class);

    private final RescueReportRepository rescueReportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final EquipmentRepository equipmentRepository;
    private final AssistingEntityRepository assistingEntityRepository;
    private final ProbableCauseRepository probableCauseRepository;
    private final RescueReportMapper rescueReportMapper;
    private final ScheduleRepository scheduleRepository;

    public RescueReportServiceImpl(RescueReportRepository rescueReportRepository, UserRepository userRepository,
                                   PostRepository postRepository, EquipmentRepository equipmentRepository,
                                   AssistingEntityRepository assistingEntityRepository,
                                   ProbableCauseRepository probableCauseRepository, RescueReportMapper rescueReportMapper, ScheduleRepository scheduleRepository) {
        this.rescueReportRepository = rescueReportRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.equipmentRepository = equipmentRepository;
        this.assistingEntityRepository = assistingEntityRepository;
        this.probableCauseRepository = probableCauseRepository;
        this.rescueReportMapper = rescueReportMapper;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public RescueReportDto createReport(RescueReportCreationDto dto) {
        log.debug("Request to create Rescue Report: {}", dto);

        UUID currentUserId = SecurityUtils.getCurrentUserUuid();
        User lifeguard = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in database."));

        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + dto.postId()));

        RescueReport report = new RescueReport();
        report.setLifeguard(lifeguard);
        report.setPost(post);
        report.setOccurrenceTimestamp(dto.occurrenceTimestamp());
        report.setOnDuty(dto.isOnDuty());
        report.setIncidentType(dto.incidentType());
        report.setIncidentTypeOther(dto.incidentTypeOther());
        report.setOutcome(dto.outcome());
        report.setVictimActivity(dto.victimActivity());
        report.setVictimActivityOther(dto.victimActivityOther());
        report.setWind(dto.wind());
        report.setVisibility(dto.visibility());
        report.setCurrent(dto.current());
        report.setTide(dto.tide());
        report.setWaves(dto.waves());
        report.setFlag(dto.flag());
        report.setEvacuation(dto.evacuation());
        report.setIncidentEndTime(dto.incidentEndTime());
        report.setAdditionalObservations(dto.additionalObservations());

        report.setEquipmentUsed(new HashSet<>(equipmentRepository.findAllById(dto.equipmentUsedIds())));
        report.setAssistingEntities(new HashSet<>(assistingEntityRepository.findAllById(dto.assistingEntitiesIds())));
        report.setProbableCauses(new HashSet<>(probableCauseRepository.findAllById(dto.probableCausesIds())));

        VictimDto victimDto = dto.victim();
        Victim victim = new Victim();
        victim.setReport(report);
        victim.setName(victimDto.name());
        victim.setNationality(victimDto.nationality());
        victim.setAddress(victimDto.address());
        victim.setPostalCode(victimDto.postalCode());
        victim.setAge(victimDto.age());
        victim.setGender(victimDto.gender());
        report.setVictim(victim);

        List<Witness> witnesses = dto.witnesses().stream().map(witnessDto -> {
            Witness witness = new Witness();
            witness.setReport(report);
            witness.setName(witnessDto.name());
            witness.setNationality(witnessDto.nationality());
            witness.setAddress(witnessDto.address());
            witness.setPostalCode(witnessDto.postalCode());
            witness.setPhoneNumber(witnessDto.phoneNumber());
            witness.setAge(witnessDto.age());
            witness.setGender(witnessDto.gender());
            witness.setSignature(witnessDto.signature());
            return witness;
        }).collect(Collectors.toList());
        report.setWitnesses(witnesses);

        RescueReport savedReport = rescueReportRepository.save(report);
        log.info("New Rescue Report created with ID: {}", savedReport.getId());

        return rescueReportMapper.toDto(savedReport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RescueReportDto> findMyReports() {
        log.debug("Request to get all reports for the current user");

        UUID currentUserId = SecurityUtils.getCurrentUserUuid();

        return rescueReportRepository.findByLifeguardIdOrderByOccurrenceTimestampDesc(currentUserId)
                .stream()
                .map(rescueReportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RescueReportDto> findReportsForBeach(Long beachId, LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get reports for beach ID {} from {} to {}", beachId, startDate, endDate);

        LocalDate localStartDate = (startDate == null) ? LocalDate.now() : startDate;
        LocalDate localEndDate = (endDate == null) ? localStartDate : endDate;

        Instant startInstant = localStartDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endInstant = localEndDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        UUID currentUserId = SecurityUtils.getCurrentUserUuid();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in database."));

        boolean isEditor = currentUser.isEditor();

        boolean isCoordinatorInPeriod = false;
        for (LocalDate date = localStartDate; date.isBefore(localEndDate); date = date.plusDays(1)) {
            if (scheduleRepository.isUserCoordinatorForBeachOnDate(currentUserId, beachId, date)) {
                isCoordinatorInPeriod = true;
                break;
            }
        }

        if (!isEditor && !isCoordinatorInPeriod) {
            throw new ForbiddenException("User does not have permission to view reports for this beach in the specified period.");
        }


        return rescueReportRepository.findByBeachAndDateRange(beachId, startInstant, endInstant)
                .stream()
                .map(rescueReportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReport(Long reportId) {
        log.debug("Request to delete Rescue Report with ID: {}", reportId);

        RescueReport report = rescueReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Rescue Report not found with ID: " + reportId));

        UUID currentUserId = SecurityUtils.getCurrentUserUuid();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in database."));

        boolean isAuthor = report.getLifeguard().getId().equals(currentUserId);
        boolean isEditor = currentUser.isEditor();

        Long beachId = report.getPost().getBeach().getId();
        LocalDate reportDate = LocalDate.ofInstant(report.getOccurrenceTimestamp(), ZoneOffset.UTC);
        boolean wasCoordinator = scheduleRepository.isUserCoordinatorForBeachOnDate(currentUserId, beachId, reportDate);

        if (isAuthor || isEditor || wasCoordinator) {
            rescueReportRepository.delete(report);
            log.info("Rescue Report with ID {} deleted successfully by user {}", reportId, currentUserId);

        }else {
            throw new ForbiddenException("User does not have permission to delete this report.");
        }

    }
}
