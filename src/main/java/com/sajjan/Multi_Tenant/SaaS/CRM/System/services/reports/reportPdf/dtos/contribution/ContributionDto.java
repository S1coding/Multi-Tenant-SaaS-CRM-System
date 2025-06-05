package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.dtos.contribution;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.Tasks;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContributionDto {
	String taskName;
	String date;
	String note;
	String contributor;

	public ContributionDto(Tasks task){
		this.date = task.getUpdatedAt().toString();
		this.taskName = task.getTitle();
		this.note = task.getNotes();
		this.contributor = task.getAssignedTo();

	}
}
