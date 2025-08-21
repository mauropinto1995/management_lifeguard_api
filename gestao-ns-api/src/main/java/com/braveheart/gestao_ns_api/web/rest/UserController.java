package com.braveheart.gestao_ns_api.web.rest;

import com.braveheart.gestao_ns_api.service.UserService;
import com.braveheart.gestao_ns_api.service.dto.UserDto;
import com.braveheart.gestao_ns_api.service.dto.UserProfileUpdateDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /users/me : Gets the profile of the currently authenticated user.
     *
     * @return the ResponseEntity with status 200 (OK) and the user's profile DTO.
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getMyProfile() {
        log.debug("REST request to get current user's profile");
        UserDto userDto = userService.getMyProfile();
        return ResponseEntity.ok(userDto);
    }

    /**
     * PUT /users/me : Updates the profile of the currently authenticated user.
     *
     * @param dto The DTO with the data to update.
     * @return the ResponseEntity with status 200 (OK) and the updated user's profile DTO.
     */
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> updateMyProfile(@Valid @RequestBody UserProfileUpdateDto dto) {
        log.debug("REST request to update current user's profile with data: {}", dto);
        UserDto updatedUser = userService.updateMyProfile(dto);
        return ResponseEntity.ok(updatedUser);
    }
}
