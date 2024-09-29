package com.imense.loneworking.application.dto.Worker;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PinSettingsUpdate {
    private String currentPin;
    private String newPin;
    private String confirmPin;
}
