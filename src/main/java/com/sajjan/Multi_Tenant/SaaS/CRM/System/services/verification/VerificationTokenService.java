package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.verification.verficationtoken;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.TenantsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationTokenService {

	@Autowired
	@Lazy
	private TenantsRepo tenantsRepo;

	public void createVerificationToken(Tenants user, String token) {
		user.setVerificationToken(token);
		tenantsRepo.save(user);
	}

	public boolean validateVerificationToken(String token) {
		Optional<Tenants> tenant = tenantsRepo.findByVerificationToken(token);
		if (tenant.isEmpty()) {
			return false;
		}

		tenant.get().setEnabled(true);
		tenantsRepo.save(tenant.get());
		return true;
	}
}
