package com.braveheart.gestao_ns_api.security;

import com.braveheart.gestao_ns_api.model.User;
import com.braveheart.gestao_ns_api.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private final SecretKey key;
    private final UserRepository userRepository;

    public TokenProvider(@Value("${supabase.jwt.secret}") String secret, UserRepository userRepository) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.userRepository = userRepository;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        UUID userId = UUID.fromString(claims.getSubject());


        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();


        List<GrantedAuthority> authorities = user.isEditor()
                ? List.of(new SimpleGrantedAuthority("ROLE_EDITOR"))
                : Collections.emptyList();

        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(
                user.getId().toString(), "", authorities
        );
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
