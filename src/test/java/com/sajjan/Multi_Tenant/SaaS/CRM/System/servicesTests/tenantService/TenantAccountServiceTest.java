package com.sajjan.Multi_Tenant.SaaS.CRM.System.servicesTests.tenantService;


import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses.TenantLoginForm;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.TenantsRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails.TenantDetails;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails.TenantDetailsService;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.tenants.TenantAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TenantAccountServiceTest {

	@Mock
	private TenantsRepo tenantsRepo;

	@Mock
	private TenantDetailsService tenantDetailsService;

	@InjectMocks
	TenantAccountService tenantAccountService;

	//sample tenants
	private Tenants tenantOne;
	private Tenants tenantTwo;

	@BeforeEach
	public void setupSampleTenants(){
		tenantOne = Tenants.builder()
				.id("test-id")
				.email("janeSmith@gmal.com")
				.firstName("jane")
				.lastName("smith")
				.password("janeSmithsPassword")
				.company("janeCorp")
				.createdAt(LocalDateTime.now())
				.phoneNumber("123123123123123")
				.authority("USER")
				.updatedAt(LocalDateTime.now())
				.build();

		tenantTwo= Tenants.builder()
				.id("test-id")
				.email("michaelOwens@gmal.com")
				.firstName("michael")
				.lastName("owens")
				.password("michaelOwensPassword")
				.company("michaelCorp")
				.createdAt(LocalDateTime.now())
				.phoneNumber("321321321321321312")
				.authority("ADMIN")
				.updatedAt(LocalDateTime.now())
				.build();
	}


	//SUCCESS CASES
	@Test
	public void tenantService_withInvalidPassword_failsLogin(){

		//ARRANGE
		String tenantOnePw = tenantOne.getPassword();
		TenantLoginForm invalidLoginForm = TenantLoginForm.builder()
				.email(tenantOne.getEmail())
				.password("incorrect_password")
				.company(tenantOne.getCompany())
				.build();
		when(tenantDetailsService.loadUserByUsername(tenantOne.getEmail()))
				.thenReturn(new TenantDetails(tenantOne));


		//Act
		boolean firstLogin = tenantAccountService.login(invalidLoginForm);


		//Assert
		Assertions.assertFalse(firstLogin);
	}

}
