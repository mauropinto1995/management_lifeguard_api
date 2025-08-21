package com.braveheart.gestao_ns_api.service.mapper;

import com.braveheart.gestao_ns_api.model.Attendance;
import com.braveheart.gestao_ns_api.service.dto.AttendanceDto;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceDto toDto(Attendance attendance) {
        if (attendance == null) {
            return null;
        }
        return new AttendanceDto(
                attendance.getId(),
                attendance.getDate(),
                attendance.getUser().getId(),
                attendance.getUser().getFirstName() + " " + attendance.getUser().getLastName(),
                attendance.getPost().getId(),
                attendance.getPost().getName(),
                attendance.getShift().getId(),
                attendance.getShift().getName(),
                attendance.getConfirmedBy().getId(),
                attendance.getConfirmedBy().getFirstName() + " " + attendance.getConfirmedBy().getLastName(),
                attendance.getObservations()
        );
    }
}
