package com.sajjan.Multi_Tenant.SaaS.CRM.System.servicesTests.tenantService;


import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.TenantLoginForm;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.TenantsRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails.TenantDetails;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails.TenantDetailsService;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.TenantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TenantServiceTest {

	@Mock
	private TenantsRepo tenantsRepo;

	@Mock
	private TenantDetailsService tenantDetailsService;

	@InjectMocks
	TenantService tenantService;

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
		boolean firstLogin = tenantService.login(invalidLoginForm);


		//Assert
		Assertions.assertFalse(firstLogin);
	}

	@Test
	public void tenantsService_withUniqueEmail_registersTenant(){

		//ARRANGE
		when(tenantsRepo.findByEmail(tenantOne.getEmail()))
				.thenReturn(Optional.empty());

		//ACT
		boolean registered = tenantService.register(tenantOne);

		//ASSERT
		Assertions.assertEquals(true, registered);
	}

	//FAIL CASES
	@Test
	public void tenantsService_withNotUniqueEmail_failsRegister() {

		//Arrange
		when(tenantsRepo.findByEmail(tenantOne.getEmail()))
				.thenReturn(Optional.empty())
				.thenReturn(Optional.of(tenantOne)); //thenReturn can be chained

		//Act
		when(tenantsRepo.save(any(Tenants.class))).thenReturn(tenantOne); //org.mockito.ArgumentMatchers.any
		boolean firstRegister = tenantService.register(tenantOne);
		boolean secondRegister = tenantService.register(tenantOne);

		//Assert
		Assertions.assertEquals(true, firstRegister);
		Assertions.assertEquals(false, secondRegister);
	}

}
