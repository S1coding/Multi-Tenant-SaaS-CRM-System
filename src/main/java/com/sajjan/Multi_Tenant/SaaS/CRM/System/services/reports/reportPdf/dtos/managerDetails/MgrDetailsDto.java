package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.managerDetails;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.Tasks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MgrDetailsDto {
	String title;
	String firstName;
	String lastName;
	String email;
	LocalDateTime date;
}
