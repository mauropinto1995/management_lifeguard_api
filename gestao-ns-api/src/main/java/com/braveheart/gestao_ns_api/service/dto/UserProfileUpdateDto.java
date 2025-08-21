package com.braveheart.gestao_ns_api.service.dto;

/**
 * DTO for updating the current user's profile information.
 * Contains only the fields a user is allowed to change.
 */
public record UserProfileUpdateDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String signatureImageUrl
) {}
