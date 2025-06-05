package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TenantUpdateDetails {
	String firstName;
	String lastName;
	String phoneNumber;
	String email;
	String password;

}
