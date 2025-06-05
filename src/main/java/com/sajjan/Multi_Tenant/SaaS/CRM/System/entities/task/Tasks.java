package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.task.wrapper.TaskDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tasks {
	@Id
	String id; //id = createdAt+assignedTo
	String title;
	String taskProject;
	String assignedTo; //TODO: spelt wrong
	String notes;
	LocalDateTime dueDate;
	String status; //TODO: Use enum instead of STRING
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
		/**
		 * Updates this task with non-null values from TaskDetails.
		 * @param taskDetails Contains the new values (null fields are ignored).
		 * @return The updated Tasks instance (this).
		 */
		public Tasks updateTaskFromDetails(TaskDetails taskDetails) {
			if (taskDetails == null) {
				return this; // No changes if details are null
			}

			// Update only non-null fields
			if (taskDetails.getTitle() != null) {
				this.title = taskDetails.getTitle();
			}
			if (taskDetails.getAssignedTo() != null) {
				this.assignedTo = taskDetails.getAssignedTo();
			}
			if (taskDetails.getNotes() != null) {
				this.notes = taskDetails.getNotes();
			}
			if (taskDetails.getDueDate() != null) {
				this.dueDate = taskDetails.getDueDate();
			}
			if (taskDetails.getStatus() != null) {
				this.status = taskDetails.getStatus();
			}
			return this;
		}
}

