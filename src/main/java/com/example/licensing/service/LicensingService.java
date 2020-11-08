package com.example.licensing.service;

import com.example.licensing.model.License;
import com.example.licensing.model.Limitation;

public interface LicensingService {

    License validate(String productKey, String serverId);

    void addProductKey(String productKey, int timeOfActivity, Limitation limitation);
}
