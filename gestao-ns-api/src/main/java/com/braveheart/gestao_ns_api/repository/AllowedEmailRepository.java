package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.AllowedEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllowedEmailRepository extends JpaRepository<AllowedEmail, Long> {

    /**
     * Finds an allowed email entry by email, but only if it has not been used yet.
     * This is the core query for our validation logic.
     * @param email The email to search for.
     * @return an Optional containing the AllowedEmail if found and not yet registered.
     */
    Optional<AllowedEmail> findByEmailAndIsRegisteredIsFalse(String email);
}
