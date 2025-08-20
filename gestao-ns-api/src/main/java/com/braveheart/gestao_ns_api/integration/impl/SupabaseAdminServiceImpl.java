package com.braveheart.gestao_ns_api.integration.impl;

import com.braveheart.gestao_ns_api.integration.SupabaseAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class SupabaseAdminServiceImpl implements SupabaseAdminService {

    private final Logger log = LoggerFactory.getLogger(SupabaseAdminServiceImpl.class);
    private final WebClient webClient;


    @Value("${supabase.api.url}")
    private String supabaseUrl;

    @Value("${supabase.api.service-role-key}")
    private String serviceRoleKey;

    public SupabaseAdminServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public void deleteUser(UUID userId) {
        String deleteUrl = supabaseUrl + "/auth/v1/admin/users/" + userId;
        log.info("Calling Supabase Admin API to delete user: {}", userId);

        webClient.delete()
                .uri(deleteUrl)
                .header("apikey", serviceRoleKey)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceRoleKey)
                .retrieve()
                .onStatus(
                httpStatus -> httpStatus.value() == 404,
                        clientResponse -> {
                        log.warn("Supabase user with ID {} not found. It might have been already deleted.", userId);
                        return Mono.empty();
                    }
                )
                .bodyToMono(Void.class)
                .doOnError(error -> log.error("Error deleting user from Supabase: {}", error.getMessage()))
                .doOnSuccess(response -> log.info("Successfully deleted user {} from Supabase Auth.", userId))
                .block();
    }
}
