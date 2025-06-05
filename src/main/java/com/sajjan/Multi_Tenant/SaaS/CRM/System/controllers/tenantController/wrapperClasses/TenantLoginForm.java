package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TenantLoginForm {
	private String email;
	private String password;
	private String company;
}
