package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Reports {

	@Id
	String id;
	String madeBy;
	//String forDeal; //?? not necessary
	String forProject;
	//String forContact; //? not necessary
	String title;
	String notes;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
}
