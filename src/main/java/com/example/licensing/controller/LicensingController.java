package com.example.licensing.controller;

import com.example.licensing.model.License;
import com.example.licensing.model.Limitation;
import com.example.licensing.service.LicensingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/licensing")
public class LicensingController {

    @Autowired
    private LicensingService licensingService;

    @PutMapping("/authorize/{productKey}/{serverId}")
    public ResponseEntity<Object> authorize(@PathVariable String productKey, @PathVariable String serverId) {
        License license = licensingService.validate(productKey, serverId);
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
                    return new ResponseEntity<>("ALREADY_USED",BAD_REQUEST);
                default:
                    return new ResponseEntity<>("INVALID", BAD_REQUEST);
            }
        }
    }

    @PostMapping("addKey/{productKey}/{timeOfActivity}")
    public ResponseEntity addProdKey(@PathVariable String productKey, @PathVariable int timeOfActivity, @RequestBody Limitation limitation) {
        licensingService.addProductKey(productKey, timeOfActivity, limitation);
        return new ResponseEntity("Key is added.", OK);
    }
}
