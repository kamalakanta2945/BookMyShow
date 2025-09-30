package com.bookmyshow.settingservice.service;

import com.bookmyshow.settingservice.dto.SettingDto;
import com.bookmyshow.settingservice.model.Setting;

import java.util.List;

public interface SettingService {

    List<Setting> getAllSettings();

    Setting getSettingByKey(String key);

    Setting updateSetting(SettingDto settingDto);
}