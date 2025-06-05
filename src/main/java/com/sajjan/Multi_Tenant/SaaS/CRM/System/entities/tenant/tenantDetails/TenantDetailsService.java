package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.TenantsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TenantDetailsService implements UserDetailsService {

	@Autowired
	private TenantsRepo tenantsRepo;

	private static final Logger logger = LoggerFactory.getLogger(TenantDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Checking if username {} exists in table", username);
		Tenants tenant = tenantsRepo.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Tenant not found with email: " + username));
		return new TenantDetails(tenant);
	}
}
