package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.task.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDetails {
	String title;
	String taskProject;
	String assignedTo;
	String notes;
	LocalDateTime dueDate;
	String status;
}
