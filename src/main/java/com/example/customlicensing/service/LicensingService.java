package com.example.customlicensing.service;

import com.example.customlicensing.model.License;
import com.example.customlicensing.model.Limitation;

public interface LicensingService {

    License validate(String productKey, String serverId);

    void addProductKey(String productKey, int timeOfActivity, Limitation limitation);
}
