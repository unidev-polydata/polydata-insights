package com.unidev.polyinsights.service;


import com.unidev.polyinsights.model.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing tenants
 */
@Repository
public interface TenantDAO  extends MongoRepository<Tenant, String> {
}
