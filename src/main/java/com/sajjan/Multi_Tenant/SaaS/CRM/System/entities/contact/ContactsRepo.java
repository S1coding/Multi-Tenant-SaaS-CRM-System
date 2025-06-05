package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactsRepo extends JpaRepository<Contacts, String> {

	List<Contacts> findByEmail(String email);
	Optional<List<Contacts>> findByTenant(String tenant);
	Optional<Contacts> findByEmailAndTenant(String contactEmail, String tenantEmail);
}
