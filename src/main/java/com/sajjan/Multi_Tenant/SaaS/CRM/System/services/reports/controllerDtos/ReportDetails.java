package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.controllerDtos;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.contact.Contacts;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.Reports;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDetails {
	String madeBy;
	String forProject;
	String title;
	String notes;

	public Reports convertToReports(ObjectMapper objectMapper) throws JsonMappingException {
		Reports reports = new Reports();
		objectMapper.updateValue(reports, this);
		return reports;
	}
}
