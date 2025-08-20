package com.braveheart.gestao_ns_api.web.rest;

import com.braveheart.gestao_ns_api.service.UserService;
import com.braveheart.gestao_ns_api.service.dto.UserWebhookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks") // All endpoints in this class will start with /api/webhooks
public class WebhookController {

    private final Logger log = LoggerFactory.getLogger(WebhookController.class);

    private final UserService userService;

    // We inject the service that contains our business logic
    public WebhookController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /sync-user : Endpoint to receive new user data from the Supabase webhook.
     *
     * @param dto the UserWebhookDto containing the new user's ID and email.
     * @return a ResponseEntity with status 200 (OK) if the request is accepted.
     */
    @PostMapping("/sync-user")
    public ResponseEntity<Void> syncNewUser(@RequestBody UserWebhookDto dto) {
        log.info("REST request received from webhook to sync user with ID: {}", dto.id());

        userService.syncNewUser(dto);

        return ResponseEntity.ok().build();
    }
}
