package com.sajjan.Multi_Tenant.SaaS.CRM.System.dbServices;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;

public class TenantContext {
	private static final ThreadLocal<Tenants> currentTenant = new InheritableThreadLocal<>();

	public static String getCurrentTenant() {
		return currentTenant.get().getId();
	}

	public static String getCurrentTenantCompany(){
		return currentTenant.get().getCompany();
	}

	public static String getCurrentTenantAuthority(){
		return currentTenant.get().getAuthority();
	}

	public static void setCurrentTenant(Tenants tenant) {
		currentTenant.set(tenant);
	}

	public static void clear() {
		currentTenant.remove();
	}
}