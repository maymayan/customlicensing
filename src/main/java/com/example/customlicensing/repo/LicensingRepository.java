package com.example.customlicensing.repo;

import com.example.customlicensing.model.License;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LicensingRepository extends MongoRepository<License, String> {
}
