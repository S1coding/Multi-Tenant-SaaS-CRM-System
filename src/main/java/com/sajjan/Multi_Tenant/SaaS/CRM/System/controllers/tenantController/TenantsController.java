package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.contactsController.wrapperClasses.StringWrapper;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses.TenantLoginForm;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses.TenantFullName;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses.TenantUpdateDetails;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.security.filters.jwt.JwtResponse;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.tenants.TenantAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenant")
public class TenantsController {

	@Autowired
	private TenantAccountService tenantAccountService;
	Logger logger = LoggerFactory.getLogger(TenantsController.class);

	@PostMapping("/login") //remove
	public ResponseEntity loginTenant(@RequestBody TenantLoginForm tenantLoginForm){
		boolean validLogin = tenantAccountService.login(tenantLoginForm);
		ResponseEntity response = validLogin ? ResponseEntity.ok("Correct credentials") : ResponseEntity.badRequest().body("Invalid credentials");
		logger.info("Endpoint /login entered for {} of company {}, response status is {}", tenantLoginForm.getEmail(), tenantLoginForm.getCompany(), response.getStatusCode());
		return response;
	}


	@PostMapping("/register") //remove
	public ResponseEntity registerTenant(@RequestBody Tenants tenant){

		boolean validRegister = tenantAccountService.register(tenant);
		ResponseEntity response = validRegister ?
				ResponseEntity.ok("Tenant successfully registered") :
				ResponseEntity.badRequest().body("Tenant with email "+tenant.getEmail()+" already exists");

		return response;
	}

	@PostMapping("/promoteTenant")
	public ResponseEntity promoteTenant(@RequestBody StringWrapper stringWrapper){
		String email = stringWrapper.getData();
		boolean success = tenantAccountService.promoteTenant(email);

		ResponseEntity response = success ?
				ResponseEntity.ok("Promoted tenant if not already highest possible role")
				: ResponseEntity.badRequest().body("Promotion failed, make sure the credentials are correct");
		return response;
	}

	@PostMapping("/generateJwtToken")
	public ResponseEntity<?> generateJwtToken(@RequestBody TenantLoginForm tenantLoginForm) {
		logger.info("Controller endpoint /generateJwtToken accessed");
		try {
			JwtResponse jwt = tenantAccountService.generateJwtTokenForAccount(tenantLoginForm);
			return ResponseEntity.ok(jwt);
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body("Invalid credentials entered");
		}
	}

	//new project to test
	@GetMapping("/getTenantsByName")
	public ResponseEntity<?> getTenantsByName(@RequestBody TenantFullName tenantFullName) {
		List<TenantFullName> names = tenantAccountService.getTenantNames();
		return ResponseEntity.ok(names);
	}

	@PostMapping("/updateTenantDetails")
	public ResponseEntity<?> updateTenantDetails(@RequestBody TenantUpdateDetails tenantUpdateDetails){
		boolean success = tenantAccountService.successfullyChangeTenantDetails(tenantUpdateDetails);
		ResponseEntity response = success ?
				ResponseEntity.ok("Tenant details successfully updated")
				: ResponseEntity.badRequest().body("Failed to update tenant details, new email is probably already linked to an account");
		return response;
	}


	}
