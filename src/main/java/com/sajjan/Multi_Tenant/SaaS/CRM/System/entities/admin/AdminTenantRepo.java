package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.masterschema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTenantRepo extends JpaRepository<AdminTenant, String> {
}
