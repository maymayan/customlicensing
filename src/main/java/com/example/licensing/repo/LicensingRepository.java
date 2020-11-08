package com.example.licensing.repo;

import com.example.licensing.model.License;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LicensingRepository extends MongoRepository<License, String> {
}
