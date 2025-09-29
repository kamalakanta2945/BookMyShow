package com.eventbook.settingservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.settingservice.model.Setting;
import com.eventbook.settingservice.service.SettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settings")
@PreAuthorize("hasRole('ADMIN')")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Setting>>> getAllSettings() {
        List<Setting> settings = settingService.getAllSettings();
        return new ResponseEntity<>(new ApiResponse<>("Settings fetched successfully", settings), HttpStatus.OK);
    }

    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse<Setting>> getSettingByKey(@PathVariable String key) {
        Setting setting = settingService.getSettingByKey(key);
        return new ResponseEntity<>(new ApiResponse<>("Setting fetched successfully", setting), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Setting>> createSetting(@Valid @RequestBody Setting setting) {
        Setting createdSetting = settingService.createSetting(setting);
        return new ResponseEntity<>(new ApiResponse<>("Setting created successfully", createdSetting), HttpStatus.CREATED);
    }

    @PutMapping("/{key}")
    public ResponseEntity<ApiResponse<Setting>> updateSetting(@PathVariable String key, @Valid @RequestBody Setting settingDetails) {
        Setting updatedSetting = settingService.updateSetting(key, settingDetails);
        return new ResponseEntity<>(new ApiResponse<>("Setting updated successfully", updatedSetting), HttpStatus.OK);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<ApiResponse<Void>> deleteSetting(@PathVariable String key) {
        settingService.deleteSetting(key);
        return new ResponseEntity<>(new ApiResponse<>("Setting deleted successfully"), HttpStatus.NO_CONTENT);
    }
}