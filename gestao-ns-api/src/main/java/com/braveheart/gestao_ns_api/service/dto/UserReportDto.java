package com.braveheart.gestao_ns_api.service.dto;

import java.util.UUID;

public record UserReportDto(
        UUID id,
        String name,
        String nationality,
        String gender,
        Integer age,
        String phoneNumber,
        Integer lifeguardNumber,
        String signatureImageUrl,
        String city
) {}
