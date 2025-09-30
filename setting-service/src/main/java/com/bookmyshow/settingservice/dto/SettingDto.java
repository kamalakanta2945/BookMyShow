package com.bookmyshow.settingservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SettingDto {

    @NotBlank(message = "Setting key cannot be blank")
    private String settingKey;

    @NotBlank(message = "Setting value cannot be blank")
    private String settingValue;
}