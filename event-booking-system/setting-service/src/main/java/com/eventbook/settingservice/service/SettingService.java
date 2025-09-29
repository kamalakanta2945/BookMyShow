package com.eventbook.settingservice.service;

import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.settingservice.model.Setting;
import com.eventbook.settingservice.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> getAllSettings() {
        return settingRepository.findAll();
    }

    public Setting getSettingByKey(String key) {
        return settingRepository.findBySettingKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting not found with key: " + key));
    }

    public Setting createSetting(Setting setting) {
        // Optional: Check if key already exists
        return settingRepository.save(setting);
    }

    public Setting updateSetting(String key, Setting settingDetails) {
        Setting setting = getSettingByKey(key);
        setting.setSettingValue(settingDetails.getSettingValue());
        setting.setDescription(settingDetails.getDescription());
        return settingRepository.save(setting);
    }

    public void deleteSetting(String key) {
        Setting setting = getSettingByKey(key);
        settingRepository.delete(setting);
    }
}