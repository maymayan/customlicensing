package com.example.licensing.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class License {
    @Id
    private String productKey;
    private String serverId;
    private int remainingTimeOfLicense;
    private boolean activated;
    private Date activationDate;
    private Date expirationDate;
    private LicenseState licenseState;
    private Limitation limitation;
}