package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.TenantAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant")
public class TenantController {

	@Autowired
	private TenantAccountService tenantAccountService;
	Logger logger = LoggerFactory.getLogger(TenantController.class);

	@PostMapping("/login")
	public ResponseEntity loginTenant(@RequestBody TenantLoginForm tenantLoginForm){
		boolean validLogin = tenantAccountService.login(tenantLoginForm);
		ResponseEntity response = validLogin ? ResponseEntity.ok("Correct credentials") : ResponseEntity.badRequest().body("Invalid credentials");
		logger.info("Endpoint /login entered for {} of company {}, response status is {}", tenantLoginForm.getEmail(), tenantLoginForm.getCompany(), response.getStatusCode());
		return response;
	}

	@PostMapping("/register")
	public ResponseEntity registerTenant(@RequestBody Tenants tenant){

		boolean validRegister = tenantAccountService.register(tenant);
		ResponseEntity response = validRegister ?
				ResponseEntity.ok("Tenant successfully registered") :
				ResponseEntity.badRequest().body("Tenant with email "+tenant.getEmail()+" already exists");

		return response;
	}

}
