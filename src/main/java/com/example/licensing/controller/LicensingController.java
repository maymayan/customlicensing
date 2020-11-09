package com.example.licensing.controller;

import com.example.licensing.model.License;
import com.example.licensing.model.Limitation;
import com.example.licensing.service.LicensingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/licensing")
public class LicensingController {

    @Autowired
    private LicensingService licensingService;

    @PutMapping("/authorize/{productKey}/{serverId}")
    public ResponseEntity<Object> authorize(@PathVariable String productKey, @PathVariable String serverId) {
        License license = licensingService.validate(getHashOf(productKey), serverId);
        if (license == null) {
            return new ResponseEntity<>("NOT_FOUND", BAD_REQUEST);
        } else {
            switch (license.getLicenseState()) {
                case EXPIRED:
                    return new ResponseEntity<>("EXPIRED", BAD_REQUEST);
                case ACTIVE:
                    return new ResponseEntity<>(license, OK);
                case CREATED:
                    return new ResponseEntity<>(license, CREATED);
                case ALREADY_USED:
                    return new ResponseEntity<>("ALREADY_USED", BAD_REQUEST);
                default:
                    return new ResponseEntity<>("INVALID", BAD_REQUEST);
            }
        }
    }

    @PostMapping("addKey/{productKey}/{timeOfActivity}")
    public ResponseEntity addProdKey(@PathVariable String productKey, @PathVariable int timeOfActivity, @RequestBody Limitation limitation) {
        licensingService.addProductKey(getHashOf(productKey), timeOfActivity, limitation);
        return new ResponseEntity("Key is added.", OK);
    }

    private String getHashOf(String productKey) {
        String productKeyHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(productKey.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            productKeyHash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return productKeyHash;
    }
}
