package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.contact.contactDetails;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.contact.Contacts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDetails {

	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String company;
	private String position;
	private String notes;

	//better to use spring object mapper to avoid potential misconfiguration problems and springs object mapper is probably
	//better designed and more complex than mine
	public Contacts convertToContacts(ObjectMapper objectMapper) throws JsonMappingException {
		Contacts contact = new Contacts();
		objectMapper.updateValue(contact, this);
		return contact;
	}
}
