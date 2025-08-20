package com.braveheart.gestao_ns_api.service.impl;

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
import com.braveheart.gestao_ns_api.service.ScheduleService;
import com.braveheart.gestao_ns_api.service.dto.ScheduleCreationDto;
import com.braveheart.gestao_ns_api.service.dto.ScheduleDto;
import com.braveheart.gestao_ns_api.service.mapper.ScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ShiftRepository shiftRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               UserRepository userRepository,
                               PostRepository postRepository,
                               ShiftRepository shiftRepository,
                               ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.shiftRepository = shiftRepository;
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
}
