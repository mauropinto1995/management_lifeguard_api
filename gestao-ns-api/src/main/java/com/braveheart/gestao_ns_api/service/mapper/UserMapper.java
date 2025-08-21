package com.braveheart.gestao_ns_api.service.mapper;

import com.braveheart.gestao_ns_api.model.User;
import com.braveheart.gestao_ns_api.service.dto.UserRegularDto;
import com.braveheart.gestao_ns_api.service.dto.UserReportDto;

public class UserMapper {

    public UserRegularDto toUserRegularDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserRegularDto(
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.isEditor()
        );

    }

    /*public UserReportDto  toUserReportDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserReportDto(
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getNationality(),
                user.getGender(),
                user.
                );
    }*/
}
