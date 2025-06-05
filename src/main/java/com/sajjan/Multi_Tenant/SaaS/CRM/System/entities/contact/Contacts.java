package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.contact;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.contactsController.wrapperClasses.ContactDetails;
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
public class Contacts {
	@Id
	String id;
	String firstName;
	String lastName;
	String email;
	String phoneNumber;
	String company;
	String position;
	String notes;
	String tenant;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;

	public void updateFromContactDetailsIfNotNull(ContactDetails details) {
		if (details.getFirstName() != null) {
			this.setFirstName(details.getFirstName());
		}
		if (details.getLastName() != null) {
			this.setLastName(details.getLastName());
		}
		if (details.getPhoneNumber() != null) {
			this.setPhoneNumber(details.getPhoneNumber());
		}
		if (details.getCompany() != null) {
			this.setCompany(details.getCompany());
		}
		if (details.getPosition() != null) {
			this.setPosition(details.getPosition());
		}
		if (details.getNotes() != null) {
			this.setNotes(details.getNotes());
		}
	}
}
