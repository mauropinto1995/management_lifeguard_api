package com.braveheart.gestao_ns_api.service;

import java.time.LocalDate;

public interface SettingsService {

    /**
     * Gets the date until which schedules are visible.
     * @return The LocalDate of visibility.
     */
    LocalDate getScheduleVisibilityDate();

    /**
     * Updates the date until which schedules are visible.
     * This method would be protected and only accessible by editors.
     * @param newDate The new visibility date.
     */
    void updateScheduleVisibilityDate(LocalDate newDate);
}
