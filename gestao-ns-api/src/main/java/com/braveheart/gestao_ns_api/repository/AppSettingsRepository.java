package com.braveheart.gestao_ns_api.repository;

import com.braveheart.gestao_ns_api.model.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppSettingsRepository extends JpaRepository<AppSettings, Long> {

    /**
     * Finds a setting by its unique key.
     * @param settingKey The key to search for (e.g., "schedule_visibility_date").
     * @return an Optional containing the AppSettings if found.
     */
    Optional<AppSettings> findBySettingKey(String settingKey);
}
