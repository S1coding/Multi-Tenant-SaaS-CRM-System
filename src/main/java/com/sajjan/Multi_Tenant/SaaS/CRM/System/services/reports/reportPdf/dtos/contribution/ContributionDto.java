package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.contribution;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.Tasks;

import java.time.LocalDateTime;

public class ContributionDto {
	String taskName;
	LocalDateTime date;
	String note;
	String contributor;

	public ContributionDto(Tasks task){
		this.taskName = task.getTitle();
		this.date = task.getUpdatedAt();
		this.taskName = task.getNotes();
		this.contributor = task.getAssignedTo();

	}
}
