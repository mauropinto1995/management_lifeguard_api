package com.braveheart.gestao_ns_api.service.impl;

import com.braveheart.gestao_ns_api.model.AppSettings;
import com.braveheart.gestao_ns_api.repository.AppSettingsRepository;
import com.braveheart.gestao_ns_api.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class SettingsServiceImpl implements SettingsService {

    private static final String VISIBILITY_KEY = "schedule_visibility_date";
    private final Logger log = LoggerFactory.getLogger(SettingsServiceImpl.class);
    private final AppSettingsRepository appSettingsRepository;

    public SettingsServiceImpl(AppSettingsRepository appSettingsRepository) {
        this.appSettingsRepository = appSettingsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public LocalDate getScheduleVisibilityDate() {
        log.debug("Request to get schedule visibility date");
        return appSettingsRepository.findBySettingKey(VISIBILITY_KEY)
                .map(setting -> LocalDate.parse(setting.getSettingValue(), DateTimeFormatter.ISO_LOCAL_DATE))
                .orElse(LocalDate.of(1900, 1, 1));
    }

    @Override
    public void updateScheduleVisibilityDate(LocalDate newDate) {
        log.info("Request to update schedule visibility date to: {}", newDate);
        AppSettings setting = appSettingsRepository.findBySettingKey(VISIBILITY_KEY)
                .orElseThrow(() -> new RuntimeException("Setting " + VISIBILITY_KEY + " not found in database!"));

        setting.setSettingValue(newDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        appSettingsRepository.save(setting);
        log.info("Schedule visibility date updated successfully.");
    }
}
