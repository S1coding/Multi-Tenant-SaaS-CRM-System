package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.dtos.contributorsDetails;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import lombok.Data;

@Data
public class ContributorDetailsDto {
	private String firstName;
	private String lastName;
	private String email;

	public ContributorDetailsDto(Tenants tenants){
		this.firstName = tenants.getFirstName();
		this.lastName = tenants.getLastName();
		this.email = tenants.getEmail();
	}
}
