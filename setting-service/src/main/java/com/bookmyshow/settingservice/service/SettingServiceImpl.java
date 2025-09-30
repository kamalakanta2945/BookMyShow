package com.bookmyshow.settingservice.service;

import com.bookmyshow.settingservice.dto.SettingDto;
import com.bookmyshow.settingservice.exception.ResourceNotFoundException;
import com.bookmyshow.settingservice.model.Setting;
import com.bookmyshow.settingservice.repository.SettingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    private static final Logger log = LoggerFactory.getLogger(SettingServiceImpl.class);

    @Autowired
    private SettingRepository settingRepository;

    @Override
    @Cacheable("allSettings")
    public List<Setting> getAllSettings() {
        log.info("Fetching all settings from database");
        return settingRepository.findAll();
    }

    @Override
    @Cacheable(value = "settings", key = "#key")
    public Setting getSettingByKey(String key) {
        log.info("Fetching setting for key: '{}' from database", key);
        return settingRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting not found with key: " + key));
    }

    @Override
    @CacheEvict(value = {"settings", "allSettings"}, allEntries = true)
    public Setting updateSetting(SettingDto settingDto) {
        log.info("Updating setting for key: '{}'", settingDto.getSettingKey());
        Setting settingToSave = mapToEntity(settingDto);
        Setting savedSetting = settingRepository.save(settingToSave);
        log.info("Successfully updated setting for key: '{}'", savedSetting.getSettingKey());
        return savedSetting;
    }

    private Setting mapToEntity(SettingDto dto) {
        Setting setting = new Setting();
        setting.setSettingKey(dto.getSettingKey());
        setting.setSettingValue(dto.getSettingValue());
        return setting;
    }
}