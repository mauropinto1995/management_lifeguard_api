package com.braveheart.gestao_ns_api.service.impl;

import com.braveheart.gestao_ns_api.exception.ResourceNotFoundException;
import com.braveheart.gestao_ns_api.integration.SupabaseAdminService;
import com.braveheart.gestao_ns_api.model.AllowedEmail;
import com.braveheart.gestao_ns_api.model.User;
import com.braveheart.gestao_ns_api.repository.AllowedEmailRepository;
import com.braveheart.gestao_ns_api.repository.UserRepository;
import com.braveheart.gestao_ns_api.security.SecurityUtils;
import com.braveheart.gestao_ns_api.service.UserService;
import com.braveheart.gestao_ns_api.service.dto.UserDto;
import com.braveheart.gestao_ns_api.service.dto.UserProfileUpdateDto;
import com.braveheart.gestao_ns_api.service.dto.UserWebhookDto;
import com.braveheart.gestao_ns_api.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final AllowedEmailRepository allowedEmailRepository;
    private final SupabaseAdminService supabaseAdminService;
    private final UserMapper userMapper;


    public UserServiceImpl(UserRepository userRepository,
                           AllowedEmailRepository allowedEmailRepository,
                           SupabaseAdminService supabaseAdminService,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.allowedEmailRepository = allowedEmailRepository;
        this.supabaseAdminService = supabaseAdminService;
        this.userMapper = userMapper;
    }

    /**
     * Synchronizes a new user from Supabase Auth into our local database.
     * This method will be called by our WebhookController.
     */
    @Override
    public void syncNewUser(UserWebhookDto dto) {
        log.info("Starting synchronization for new user with email: {}", dto.email());

        Optional<AllowedEmail> allowedEmailOpt = allowedEmailRepository.findByEmailAndIsRegisteredIsFalse(dto.email());

        if (allowedEmailOpt.isEmpty()) {
            log.warn("Unauthorized registration attempt for email: {}. This email is not on the allowlist or has already been used.", dto.email());
            supabaseAdminService.deleteUser(dto.id());
            return;
        }


        if (userRepository.existsById(dto.id())) {
            log.warn("Attempted to sync a user that already exists in the system. ID: {}", dto.id());
            return;
        }


        User newUser = new User();
        newUser.setId(dto.id());
        newUser.setEmail(dto.email());


        newUser.setFirstName("Utilizador");
        newUser.setLastName("Novo");
        newUser.setNationality("A preencher");
        newUser.setGender("masculino");
        newUser.setDateOfBirth(LocalDate.of(1900, 1, 1));
        newUser.setPhoneNumber("000000000");
        newUser.setLifeguardNumber(0);
        newUser.setEditor(false);

        userRepository.save(newUser);

        log.info("New user synchronized and saved successfully. ID: {}", newUser.getId());

        AllowedEmail allowedEmail = allowedEmailOpt.get();
        allowedEmail.setRegistered(true);
        allowedEmailRepository.save(allowedEmail);
        log.info("Email {} has been marked as registered.", allowedEmail.getEmail());
    }


    @Override
    @Transactional(readOnly = true)
    public User findById(UUID userId) {
        log.debug("Searching for user with ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getMyProfile() {
        UUID currentUserId = SecurityUtils.getCurrentUserUuid();
        return userRepository.findById(currentUserId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found in database."));
    }

    @Override
    public UserDto updateMyProfile(UserProfileUpdateDto dto) {
        UUID currentUserId = SecurityUtils.getCurrentUserUuid();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found in database."));


        currentUser.setFirstName(dto.firstName());
        currentUser.setLastName(dto.lastName());
        currentUser.setPhoneNumber(dto.phoneNumber());
        currentUser.setSignatureImageUrl(dto.signatureImageUrl());

        User updatedUser = userRepository.save(currentUser);
        log.info("User profile updated for user ID: {}", currentUserId);

        return userMapper.toDto(updatedUser);
    }
}
