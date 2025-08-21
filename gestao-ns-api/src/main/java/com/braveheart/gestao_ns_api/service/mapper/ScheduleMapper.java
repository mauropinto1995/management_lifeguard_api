package com.braveheart.gestao_ns_api.service.mapper;

import com.braveheart.gestao_ns_api.model.Schedule;
import com.braveheart.gestao_ns_api.service.dto.ScheduleDto;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public ScheduleDto toDto(Schedule schedule) {
        if (schedule == null) {
            return null;
        }
        return new ScheduleDto(
                schedule.getId(),
                schedule.getDate(),
                schedule.getShift().getStartTime(),
                schedule.getShift().getEndTime(),
                schedule.getPost().getName(),
                schedule.getUser().getId(),
                schedule.getUser().getFirstName(),
                schedule.getUser().getLastName(),
                "VISIBLE"
        );
    }
}
