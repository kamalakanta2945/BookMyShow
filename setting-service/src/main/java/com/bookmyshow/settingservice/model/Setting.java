package com.bookmyshow.settingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String settingKey;
    private String settingValue;
}