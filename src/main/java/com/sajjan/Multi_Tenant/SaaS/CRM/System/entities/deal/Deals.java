package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.deal;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.dealsController.wrapperClasses.DealsDetails;
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
public class Deals {

	@Id
	String id;
	String madeBy;
	String madeTo;
	String notes;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;

	public void updateDetailsToDeal(DealsDetails details) {
		if (details.getMadeBy() != null) {
			this.setMadeBy(details.getMadeBy());
		}
		if (details.getMadeTo() != null) {
			this.setMadeTo(details.getMadeTo());
		}

		if (details.getNotes() != null) {
			this.setNotes(details.getNotes());
		}
	}
}
