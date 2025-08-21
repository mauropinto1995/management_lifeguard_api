package com.braveheart.gestao_ns_api.service.dto;

import java.util.UUID;

/**
 * DTO for representing a user's public profile data.
 */
public record UserDto(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String signatureImageUrl,
        boolean isEditor
) {}
