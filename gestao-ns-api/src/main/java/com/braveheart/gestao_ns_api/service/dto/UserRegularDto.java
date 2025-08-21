package com.braveheart.gestao_ns_api.service.dto;

import java.util.UUID;

public record UserRegularDto (
        UUID id,
        String name,
        boolean is_editor
) {}
