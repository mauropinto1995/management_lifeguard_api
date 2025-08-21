package com.braveheart.gestao_ns_api.service.impl;

import com.braveheart.gestao_ns_api.exception.ForbiddenException;
import com.braveheart.gestao_ns_api.exception.ResourceNotFoundException;
import com.braveheart.gestao_ns_api.model.*;
import com.braveheart.gestao_ns_api.repository.*;
import com.braveheart.gestao_ns_api.security.SecurityUtils;
import com.braveheart.gestao_ns_api.service.AttendanceService;
import com.braveheart.gestao_ns_api.service.dto.AttendanceCreationDto;
import com.braveheart.gestao_ns_api.service.dto.AttendanceDto;
import com.braveheart.gestao_ns_api.service.dto.AttendanceUpdateDto;
import com.braveheart.gestao_ns_api.service.mapper.AttendanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final Logger log = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ShiftRepository shiftRepository;
    private final AttendanceMapper attendanceMapper;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, ScheduleRepository scheduleRepository,
                                 UserRepository userRepository, PostRepository postRepository,
                                 ShiftRepository shiftRepository, AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.shiftRepository = shiftRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public AttendanceDto createAttendance(AttendanceCreationDto dto) {
        log.debug("Request to create Attendance: {}", dto);


        UUID currentUserId = SecurityUtils.getCurrentUserUuid();
        User coordinator = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Coordinator user not found in database."));


        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + dto.postId()));
        Long beachId = post.getBeach().getId();


        boolean isCoordinator = scheduleRepository.isUserCoordinatorForBeachOnDate(currentUserId, beachId, dto.date());

        if (!isCoordinator) {
            throw new ForbiddenException("User is not the coordinator for this beach on the specified date.");
        }


        User presentUser = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Present user not found with ID: " + dto.userId()));
        Shift shift = shiftRepository.findById(dto.shiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + dto.shiftId()));


        Attendance newAttendance = new Attendance();
        newAttendance.setDate(dto.date());
        newAttendance.setUser(presentUser);
        newAttendance.setPost(post);
        newAttendance.setShift(shift);
        newAttendance.setConfirmedBy(coordinator);
        newAttendance.setObservations(dto.observations());


        if (dto.scheduleId() != null) {
            scheduleRepository.findById(dto.scheduleId()).ifPresent(newAttendance::setSchedule);
        }


        Attendance savedAttendance = attendanceRepository.save(newAttendance);
        log.info("New Attendance created with ID: {}", savedAttendance.getId());

        return attendanceMapper.toDto(savedAttendance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> findAttendancesForBeach(Long beachId, LocalDate date) {
        log.debug("Request to get attendances for beach ID {} on date {}", beachId, date);
        LocalDate searchDate = (date == null) ? LocalDate.now() : date;

        // 1. Get the user making the request
        UUID currentUserId = SecurityUtils.getCurrentUserUuid();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in database."));

        // 2. Authorization Logic: Check if user is an editor OR the coordinator for that beach on that day
        boolean isEditor = currentUser.isEditor();
        boolean isCoordinator = scheduleRepository.isUserCoordinatorForBeachOnDate(currentUserId, beachId, searchDate);

        if (!isEditor && !isCoordinator) {
            throw new ForbiddenException("User does not have permission to view attendances for this beach.");
        }

        // 3. If authorized, fetch the data
        return attendanceRepository.findByBeachAndDate(beachId, searchDate)
                .stream()
                .map(attendanceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> findAttendancesForBeachByMonth(Long beachId, int year, int month) {
        log.debug("Request to get monthly attendances for beach ID {} for year/month {}/{}", beachId, year, month);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return attendanceRepository.findByBeachAndMonth(beachId, startDate, endDate)
                .stream()
                .map(attendanceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDto updateAttendance(Long id, AttendanceUpdateDto dto) {
        log.debug("Request to update Attendance with ID {}: {}", id, dto);

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + id));

        User presentUser = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Present user not found with ID: " + dto.userId()));
        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + dto.postId()));
        Shift shift = shiftRepository.findById(dto.shiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + dto.shiftId()));

        attendance.setUser(presentUser);
        attendance.setPost(post);
        attendance.setShift(shift);
        attendance.setDate(dto.date());
        attendance.setObservations(dto.observations());

        Attendance updatedAttendance = attendanceRepository.save(attendance);
        log.info("Attendance record with ID {} updated successfully.", id);

        return attendanceMapper.toDto(updatedAttendance);
    }
}
