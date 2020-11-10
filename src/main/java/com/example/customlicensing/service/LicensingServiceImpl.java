package com.example.customlicensing.service;

import com.example.customlicensing.model.License;
import com.example.customlicensing.model.Limitation;
import com.example.customlicensing.repo.LicensingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.customlicensing.model.LicenseState.*;

@Service
public class LicensingServiceImpl implements LicensingService {

    @Autowired
    private LicensingRepository licensingRepository;

    @Override
    public License validate(String productKey, String serverId) {
        Optional<License> license = licensingRepository.findById(productKey);
        if (!license.isPresent()) {
            return null;
        } else {

            License license1 = license.get();
            if (license1.isActivated()) {

                if (license1.getServerId()!=null && !license1.getServerId().equals(serverId)) {
                    License used = new License();
                    used.setLicenseState(ALREADY_USED);
                    return used;
                }
                if (license1.getExpirationDate().after(new Date(System.currentTimeMillis()))) {
                    license1.setRemainingTimeOfLicense((int) TimeUnit.DAYS.convert(license1.getExpirationDate().getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
                    license1.setLicenseState(ACTIVE);
                    licensingRepository.save(license1);
                    return license1;
                } else {
                    license1.setActivated(false);
                    license1.setRemainingTimeOfLicense(0);
                    license1.setLicenseState(EXPIRED);
                    licensingRepository.save(license1);
                    return license1;
                }
            } else {
                if (license1.getLicenseState() != EXPIRED) {
                    license1.setServerId(serverId);
                    license1.setActivationDate(new Date(System.currentTimeMillis()));
                    license1.setActivated(true);
                    license1.setExpirationDate(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(license1.getRemainingTimeOfLicense(), TimeUnit.DAYS)));
                    license1.setLicenseState(CREATED);
                    licensingRepository.save(license1);
                    return license1;
                } else {
                    License expired = new License();
                    expired.setLicenseState(EXPIRED);
                    return expired;
                }
            }
        }
    }

    @Override
    public void addProductKey(String productKey, int timeOfActivity, Limitation limitation) {
        License license = new License();
        license.setProductKey(productKey);
        license.setRemainingTimeOfLicense(timeOfActivity);
        license.setActivated(false);
        license.setLimitation(limitation);
        licensingRepository.save(license);
    }
}
