package com.braveheart.gestao_ns_api.web.rest;

import com.braveheart.gestao_ns_api.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final Logger log = LoggerFactory.getLogger(SettingsController.class);

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    /**
     * GET /settings/schedule-visibility : Obtém a data de visibilidade atual da escala.
     */
    @GetMapping("/schedule-visibility")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LocalDate> getScheduleVisibilityDate() {
        log.debug("REST request to get schedule visibility date");
        LocalDate visibilityDate = settingsService.getScheduleVisibilityDate();
        return ResponseEntity.ok(visibilityDate);
    }

    /**
     * PUT /settings/schedule-visibility : Atualiza a data de visibilidade da escala.
     * Apenas acessível por utilizadores com a permissão 'ROLE_EDITOR'.
     * @param newDate A nova data no formato AAAA-MM-DD.
     */
    @PutMapping("/schedule-visibility")
    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    public ResponseEntity<Void> updateScheduleVisibilityDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate) {
        log.info("REST request to update schedule visibility date to {}", newDate);
        settingsService.updateScheduleVisibilityDate(newDate);
        return ResponseEntity.ok().build();
    }
}
