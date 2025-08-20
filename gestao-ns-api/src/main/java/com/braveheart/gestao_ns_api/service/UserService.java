package com.braveheart.gestao_ns_api.service;

import com.braveheart.gestao_ns_api.exception.ResourceNotFoundException;
import com.braveheart.gestao_ns_api.model.User;
import com.braveheart.gestao_ns_api.service.dto.UserWebhookDto;

import java.util.UUID;

public interface UserService {

    /**
     * Synchronizes a new user from Supabase Auth into our local database.
     * @param dto The user data (ID and email) from the webhook.
     */
    void syncNewUser(UserWebhookDto dto);

    /**
     * Finds a user profile by its ID.
     * @param userId The ID of the user to find.
     * @return The found User object.
     * @throws ResourceNotFoundException if the user is not found.
     */
    User findById(UUID userId);
}
