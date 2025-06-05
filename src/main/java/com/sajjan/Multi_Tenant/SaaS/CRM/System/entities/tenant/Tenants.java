package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.tenantController.wrapperClasses.TenantUpdateDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tenants {

	@Id
	String id;
	String firstName;
	String lastName;
	String email;
	String password;
	String company;
	String phoneNumber;
	String authority;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;

	public Tenants updateFromDetails(TenantUpdateDetails tenantDetails) {
		if (tenantDetails == null) {
			return this;
		}

		if (tenantDetails.getFirstName() != null) {
			this.firstName = tenantDetails.getFirstName();
		}
		if (tenantDetails.getLastName() != null) {
			this.lastName = tenantDetails.getLastName();
		}
		if (tenantDetails.getEmail() != null) {
			this.email = tenantDetails.getEmail();
		}
		if (tenantDetails.getPassword() != null) {
			this.password = tenantDetails.getPassword();
		}

		if (tenantDetails.getPhoneNumber() != null) {
			this.phoneNumber = tenantDetails.getPhoneNumber();
		}

		this.updatedAt = LocalDateTime.now();

		this.authority = "USER";

		return this;
	}
}
