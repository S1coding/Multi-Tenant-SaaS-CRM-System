package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantsRepo extends JpaRepository<Tenants, String> {
	Optional<Tenants> findByEmail(String username);
}
