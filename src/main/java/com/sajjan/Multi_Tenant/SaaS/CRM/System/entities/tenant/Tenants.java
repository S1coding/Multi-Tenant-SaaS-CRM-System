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
public class Tenant {

	@Id
	java.lang.String id;
	java.lang.String firstName;
	java.lang.String lastName;
	java.lang.String email;
	java.lang.String password;
	java.lang.String company;
	java.lang.String phoneNumber;
	java.lang.String authority;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;

	public String updateFromDetails(TenantUpdateDetails tenantDetails) {
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
