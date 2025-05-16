package com.sajjan.Multi_Tenant.SaaS.CRM.System.services;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.TenantLoginForm;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.TenantsRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails.TenantDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TenantAccountService {

	@Autowired
	private TenantDetailsService tenantDetailsService;

	@Autowired
	private TenantsRepo tenantsRepo;

	private Logger logger = LoggerFactory.getLogger(TenantAccountService.class);

	//TODO:
	//LOGIN ✔️
	//REGISTER ✔️
	//CHANGE PASSWORD ❌
	//EMAIL AUTHENTICATION ❌
	//PHONE NUMBER AUTHENTICATION ❌

	public boolean login(TenantLoginForm tenantLoginForm){
		UserDetails tenantDetails = tenantDetailsService.loadUserByUsername(tenantLoginForm.getEmail());
		logger.info("Tenant with email {} exists in table", tenantLoginForm.getEmail());
		boolean passwordCheck = tenantDetails.getPassword().equals(tenantLoginForm.getPassword());
		logger.info("Tenant with email {} entered {} password in login form", tenantLoginForm.getEmail(), passwordCheck);
		return passwordCheck;
	}

	public boolean register(Tenants tenant) {
		String tenantEmail = tenant.getEmail();
		boolean tenantExists = tenantsRepo.findByEmail(tenantEmail).isPresent();

		if (tenantExists) {
			logger.warn("Registration failed: Tenant with email {} already exists.", tenantEmail);
			return false;
		}

		tenant.setCreatedAt(LocalDateTime.now());
		tenantsRepo.save(tenant);
		logger.info("Successfully registered tenant with email: {}", tenantEmail);
		return true;
	}
}
