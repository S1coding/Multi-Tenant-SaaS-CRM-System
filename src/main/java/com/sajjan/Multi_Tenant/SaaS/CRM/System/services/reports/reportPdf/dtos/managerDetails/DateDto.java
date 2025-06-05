package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.dtos.managerDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class DateDto {
	String date;

	public DateDto(LocalDateTime date){
		this.date  = date.toString();
	}
}
