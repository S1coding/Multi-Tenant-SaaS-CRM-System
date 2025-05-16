package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class TenantDetails implements UserDetails {


	private Tenants tenant;

	public TenantDetails(Tenants tenant) {
		this.tenant = tenant;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(tenant.getAuthority()));
	}

	@Override
	public String getPassword() {
		return tenant.getPassword();
	}

	@Override
	public String getUsername() {
		return tenant.getEmail();
	}
}
