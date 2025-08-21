package com.braveheart.gestao_ns_api.service.mapper;

import com.braveheart.gestao_ns_api.model.User;
import com.braveheart.gestao_ns_api.service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getSignatureImageUrl(),
                user.isEditor()
        );
    }
}
