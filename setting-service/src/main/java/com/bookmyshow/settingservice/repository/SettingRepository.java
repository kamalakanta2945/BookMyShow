package com.bookmyshow.settingservice.repository;

import com.bookmyshow.settingservice.model.Setting;

import java.util.List;
import java.util.Optional;

public interface SettingRepository {

    Optional<Setting> findByKey(String key);

    List<Setting> findAll();

    Setting save(Setting setting);
}