package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.tenants;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses.TenantFullName;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses.TenantLoginForm;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses.TenantUpdateDetails;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.TenantsRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails.TenantDetailsService;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.security.filters.jwt.JwtResponse;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.security.filters.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TenantAccountService {

	@Autowired
	private TenantDetailsService tenantDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private TenantsRepo tenantsRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private Logger logger = LoggerFactory.getLogger(TenantAccountService.class);

	//TODO:
	//LOGIN ✔️ ❌
	//REGISTER ✔️ ❌
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

		//add a more sophisticated password integrity checker
		boolean passwordNull = tenant.getPassword() == null || tenant.getPassword() == "";

		if(passwordNull){
			logger.warn("Registration failed: Tenant with email {} no valid password was given.", tenantEmail);
			return false;
		}

		String encodedPassword = passwordEncoder.encode(tenant.getPassword());
		String generatedId = generatedIdFor(tenantEmail);

		tenant.setId(generatedId);
		tenant.setCreatedAt(LocalDateTime.now());
		tenant.setUpdatedAt(LocalDateTime.now());
		tenant.setPassword(encodedPassword);
		tenant.setAuthority("USER");
		tenantsRepo.save(tenant);
		logger.info("Successfully registered tenant with email: {}", tenantEmail);
		return true;
	}

	public JwtResponse generateJwtTokenForAccount(TenantLoginForm tenantLogin){
		try {
			UsernamePasswordAuthenticationToken authToken = loginToAuthToken(tenantLogin);
			authenticationManager.authenticate(authToken);
			String jwtToken = JwtUtil.generateToken(tenantLogin.getEmail());
			logger.info("JwtResponse generated for account with email {}", tenantLogin.getEmail());
			return new JwtResponse(jwtToken);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid login credentials");
		}
	}

	public boolean successfullyChangeTenantDetails(TenantUpdateDetails tenantUpdateDetails){
		String email = getUsernameFromSecurityContext();
		String emailToUpdate = tenantUpdateDetails.getEmail();

		Optional<Tenants> doesTenantOfEmailExist = tenantsRepo.findByEmail(emailToUpdate);
		if(doesTenantOfEmailExist.isPresent()){
			logger.warn("Tried to updated tenant of email '{}' to a new email '{}', but tenant with that email already exists", email, emailToUpdate);
			return false;
		}

		Tenants tenant = tenantsRepo.findByEmail(email).get();
		tenant.updateFromDetails(tenantUpdateDetails);

		tenantsRepo.save(tenant);
		logger.info("Successfully changed tenant of email '{}' to {}", email, tenant);
		return true;
	}

	public boolean promoteTenant(String tenantEmail){
		Optional<Tenants> tenantOptional = tenantsRepo.findByEmail(tenantEmail);
		if(tenantOptional.isEmpty()){
			logger.warn("Tried to promote tenant of email '{}' but no such tenant exists", tenantEmail);
			return false;
		}
		String authority = tenantOptional.get().getAuthority();
		logger.info("Current authority is '{}'",authority);
		if(authority.equals("USER")){
			tenantOptional.get().setAuthority("MANAGER");
			logger.info("Promoted tenant '{}' form USER to MANAGER", tenantEmail);
		}
		if(authority.equals("MANAGER")){
			tenantOptional.get().setAuthority("ADMIN");
			logger.info("Promoted tenant '{}' form MANAGER to ADMIN", tenantEmail);
		}
		tenantsRepo.save(tenantOptional.get());
		return true;
	}

	public List<TenantFullName> getTenantNames(){
		List<Tenants> tenants = tenantsRepo.findAll();
		List<TenantFullName> fullNames = new ArrayList<>();
		for(Tenants tenant: tenants){
			TenantFullName fullName = new TenantFullName(tenant.getFirstName(), tenant.getLastName());
			fullNames.add(fullName);
		}
		return fullNames;
	}

	private UsernamePasswordAuthenticationToken loginToAuthToken(TenantLoginForm tenantLogin){
		return new UsernamePasswordAuthenticationToken(tenantLogin.getEmail(), tenantLogin.getPassword());
	}

	private String generatedIdFor(String email){
		return UUID.nameUUIDFromBytes(email.getBytes()).toString();
	}

	private String getUsernameFromSecurityContext(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
