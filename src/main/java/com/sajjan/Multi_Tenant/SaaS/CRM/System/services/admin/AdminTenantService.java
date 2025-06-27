package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.masterschema;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class AdminTenant {

	@Autowired
	@Lazy
	private AdminTenantRepo adminTenantRepo;


	//add new admin tenant
	public boolean successfullyAddNewTenant(AdminTenantDto adminTenantDto){
		return false;
	}

	//create schema for admin tenant -> Must have all tables
	public boolean successfullyCreateTenantSchemas(AdminTenant admin){
		return false;
	}
}
