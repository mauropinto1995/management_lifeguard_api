package com.braveheart.gestao_ns_api.service.dto;

public record VictimDto(
        String name,
        String nationality,
        String address,
        String postalCode,
        Integer age,
        String gender
) {}
