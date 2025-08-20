package com.braveheart.gestao_ns_api.integration;

import java.util.UUID;

/**
 * Service to interact with the Supabase Admin API.
 */
public interface SupabaseAdminService {

    /**
     * Deletes a user from the Supabase authentication system.
     * @param userId The UUID of the user to delete.
     */
    void deleteUser(UUID userId);
}
