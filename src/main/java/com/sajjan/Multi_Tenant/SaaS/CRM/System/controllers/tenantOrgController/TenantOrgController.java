package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenantOrg")
public class TenantOrgController {


	@PostMapping("/registerOrg")
	public ResponseEntity registerTenantOrg(){
		return  ResponseEntity.ok("for test");
	}

}
