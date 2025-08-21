package com.braveheart.gestao_ns_api.service.dto;

public record WitnessDto(
        String name,
        String nationality,
        String address,
        String postalCode,
        String phoneNumber,
        Integer age,
        String gender,
        String signature
) {}