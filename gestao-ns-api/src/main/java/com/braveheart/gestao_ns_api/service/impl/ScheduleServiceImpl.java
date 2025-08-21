package com.braveheart.gestao_ns_api.service.impl;

import com.braveheart.gestao_ns_api.exception.ForbiddenException;
import com.braveheart.gestao_ns_api.exception.ResourceNotFoundException;
import com.braveheart.gestao_ns_api.exception.ScheduleConflictException;
import com.braveheart.gestao_ns_api.model.Post;
import com.braveheart.gestao_ns_api.model.Schedule;
import com.braveheart.gestao_ns_api.model.Shift;
import com.braveheart.gestao_ns_api.model.User;
import com.braveheart.gestao_ns_api.repository.PostRepository;
import com.braveheart.gestao_ns_api.repository.ScheduleRepository;
import com.braveheart.gestao_ns_api.repository.ShiftRepository;
import com.braveheart.gestao_ns_api.repository.UserRepository;
import com.braveheart.gestao_ns_api.security.SecurityUtils;
import com.braveheart.gestao_ns_api.service.ScheduleService;
import com.braveheart.gestao_ns_api.service.SettingsService;
import com.braveheart.gestao_ns_api.service.dto.ScheduleCreationDto;
import com.braveheart.gestao_ns_api.service.dto.ScheduleDto;
import com.braveheart.gestao_ns_api.service.mapper.ScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ShiftRepository shiftRepository;
    private final SettingsService settingsService;
    private final ScheduleMapper scheduleMapper;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               UserRepository userRepository,
                               PostRepository postRepository,
                               ShiftRepository shiftRepository, SettingsService settingsService,
                               ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.shiftRepository = shiftRepository;
        this.settingsService = settingsService;
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    public ScheduleDto createSchedule(ScheduleCreationDto dto) {
        log.debug("Request to create Schedule : {}", dto);

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.userId()));

        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + dto.postId()));

        Shift shift = shiftRepository.findById(dto.shiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + dto.shiftId()));

        Schedule newSchedule = new Schedule();
        newSchedule.setUser(user);
        newSchedule.setPost(post);
        newSchedule.setShift(shift);
        newSchedule.setDate(dto.date());

        try {
            Schedule savedSchedule = scheduleRepository.save(newSchedule);
            log.info("Created new Schedule with ID: {}", savedSchedule.getId());
            return scheduleMapper.toDto(savedSchedule);
        } catch (DataIntegrityViolationException e) {

            log.warn("Schedule creation failed due to a conflict: {}", e.getMessage());
            throw new ScheduleConflictException("Schedule conflict: The user or post is already scheduled for this date and shift.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDto> findMySchedules(LocalDate startDate, LocalDate endDate) {

        UUID currentUserId = SecurityUtils.getCurrentUserUuid();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in database."));

        LocalDate maxVisibilityDate = settingsService.getScheduleVisibilityDate();

        LocalDate finalStartDate = (startDate == null) ? LocalDate.now() : startDate;
        LocalDate finalEndDate = (endDate == null) ? finalStartDate.plusDays(6) : endDate;

        LocalDate queryEndDate = finalEndDate.isAfter(maxVisibilityDate) ? maxVisibilityDate : finalEndDate;

        List<Schedule> visibleSchedules;
        if (finalStartDate.isAfter(queryEndDate)) {
            visibleSchedules = Collections.emptyList();
        } else {
            visibleSchedules = scheduleRepository
                    .findByUserAndDateBetweenOrderByDateAsc(currentUser, finalStartDate, queryEndDate);
        }


        Map<LocalDate, ScheduleDto> visibleScheduleMap = visibleSchedules.stream()
                .map(scheduleMapper::toDto)
                .collect(Collectors.toMap(ScheduleDto::date, Function.identity()));


        List<ScheduleDto> result = new ArrayList<>();
        for (LocalDate date = finalStartDate; !date.isAfter(finalEndDate); date = date.plusDays(1)) {
            if (visibleScheduleMap.containsKey(date)) {
                result.add(visibleScheduleMap.get(date));

            } else if (date.isAfter(maxVisibilityDate)) {
                result.add(ScheduleDto.createNotPublished(date));
            }
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDto> findSchedulesForBeach(Long beachId, LocalDate startDate, LocalDate endDate) {
        // 1. OBTER O UTILIZADOR AUTENTICADO
        UUID currentUserId = SecurityUtils.getCurrentUserUuid();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in database."));

        // 2. LÓGICA DE AUTORIZAÇÃO DINÂMICA
        boolean isEditor = currentUser.isEditor();
        // A verificação de coordenador é feita apenas para o dia de hoje.
        boolean isCoordinatorToday = scheduleRepository.isUserCoordinatorForBeachOnDate(currentUserId, beachId, LocalDate.now());

        if (!isEditor && !isCoordinatorToday) {
            throw new ForbiddenException("User does not have permission to view this beach's schedule.");
        }

        // 3. SE A AUTORIZAÇÃO PASSAR, CONTINUAR COM A LÓGICA EXISTENTE
        LocalDate maxVisibilityDate = settingsService.getScheduleVisibilityDate();
        LocalDate finalStartDate = (startDate == null) ? LocalDate.now() : startDate;
        LocalDate finalEndDate = (endDate == null) ? finalStartDate : endDate;

        if (finalEndDate.isAfter(maxVisibilityDate)) {
            finalEndDate = maxVisibilityDate;
        }
        if (finalStartDate.isAfter(maxVisibilityDate)) {
            return Collections.emptyList();
        }

        return scheduleRepository
                .findSchedulesByBeachAndDateRange(beachId, finalStartDate, finalEndDate)
                .stream()
                .map(scheduleMapper::toDto)
                .collect(Collectors.toList());
    }
}

