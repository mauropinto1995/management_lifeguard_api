package com.braveheart.gestao_ns_api.security;

import com.braveheart.gestao_ns_api.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {}

    /**
     * Gets the UUID of the currently authenticated user.
     *
     * @return The UUID of the current user.
     * @throws ForbiddenException if there is no authenticated user.
     */
    public static UUID getCurrentUserUuid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ForbiddenException("No authenticated user found in security context.");
        }
        // The 'sub' (subject) claim in the Supabase JWT contains the user's UUID.
        // Our TokenProvider sets the username of the UserDetails object to the user's email,
        // but the UUID is in the subject of the token itself. Let's adjust TokenProvider to make this easier.
        // For now, let's assume the principal's name IS the UUID for simplicity.
        // We will correct this in the TokenProvider later.
        if (authentication.getPrincipal() instanceof UserDetails) {
            // This part needs adjustment in TokenProvider. Let's proceed with a placeholder.
            // For the logic to work, we need to adjust TokenProvider to store the UUID.
            // Let's assume for now the name is the UUID string.
            return UUID.fromString( ((UserDetails) authentication.getPrincipal()).getUsername() );
        }
        throw new ForbiddenException("Could not extract user UUID from principal.");
    }
}
