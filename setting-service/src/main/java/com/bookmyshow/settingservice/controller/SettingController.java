package com.bookmyshow.settingservice.controller;

import com.bookmyshow.settingservice.dto.ApiResponse;
import com.bookmyshow.settingservice.dto.SettingDto;
import com.bookmyshow.settingservice.model.Setting;
import com.bookmyshow.settingservice.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SettingController {

    private static final Logger log = LoggerFactory.getLogger(SettingController.class);

    @Autowired
    private SettingService settingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Setting>>> getAllSettings() {
        log.info("GET /api/settings - retrieving all settings");
        List<Setting> settings = settingService.getAllSettings();
        ApiResponse<List<Setting>> response = new ApiResponse<>(HttpStatus.OK, "Settings retrieved successfully.", settings);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse<Setting>> getSettingByKey(@PathVariable String key) {
        log.info("GET /api/settings/{}", key);
        Setting setting = settingService.getSettingByKey(key);
        ApiResponse<Setting> response = new ApiResponse<>(HttpStatus.OK, "Setting retrieved successfully.", setting);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Setting>> updateSettings(@Valid @RequestBody SettingDto settingDto) {
        log.info("PUT /api/settings - updating setting with key: '{}'", settingDto.getSettingKey());
        Setting updatedSetting = settingService.updateSetting(settingDto);
        ApiResponse<Setting> response = new ApiResponse<>(HttpStatus.OK, "Setting updated successfully.", updatedSetting);
        return ResponseEntity.ok(response);
    }
}