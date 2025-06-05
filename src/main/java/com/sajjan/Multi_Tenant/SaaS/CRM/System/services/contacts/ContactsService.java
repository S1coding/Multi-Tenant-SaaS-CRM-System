package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.contacts;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.contact.Contacts;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.contact.ContactsRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.contactsController.wrapperClasses.ContactDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactsService {

	@Autowired
	private ContactsRepo contactsRepo;

	@Autowired
	private ObjectMapper objectMapper;

	private final Logger logger = LoggerFactory.getLogger(ContactsService.class);

	public List<Contacts> getAllSchemaContact(){
		List<Contacts> contacts = contactsRepo.findAll();
		logger.info("Retrieved list all contacts, number of contacts in list : {}", contacts.size());
		return contactsRepo.findAll();
	}

	public List<Contacts> getAllSchemaContactsWithEmail(String email){
		List<Contacts> contacts = contactsRepo.findByEmail(email);
		logger.info("Retrieved list all contacts by email {}, number of contacts in list : {}", email, contacts.size());
		return contacts;

	}

	public boolean updateContactWithCurrentTenant(ContactDetails contactDetails) throws JsonMappingException {

		String contactEmail = contactDetails.getEmail();
		String tenantEmail = getNameFromSecurityContext();

		Contacts existingContact = findContactsByEmailAndTenant(contactEmail, tenantEmail);
		if (existingContact == null) {
			logger.warn("No existing contact found for email '{}' and tenant '{}'", contactEmail, tenantEmail);
			return false;
		}

		LocalDateTime localDateTime = LocalDateTime.now();
		existingContact.updateFromContactDetailsIfNotNull(contactDetails); //does not update -> id, email, tenant, timestamps
		existingContact.setUpdatedAt(localDateTime);

		logger.info("Mapping updateContact to existingContact of id '{}' for tenant '{}'", existingContact.getId(), tenantEmail);
		Contacts updatedContact = contactsRepo.save(existingContact);
		logger.info("Successfully updated contact with email '{}' and tenant '{}'", contactEmail, tenantEmail);

		return true;
	}

	public Contacts createContactWithCurrentTenant(ContactDetails contactDetails) throws JsonMappingException { //uses security context

		String tenantEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		Contacts contact = contactDetails.convertToContacts(objectMapper);

		String UniqueId = generatedIdFrom(contact.getEmail()+tenantEmail);
		LocalDateTime localDateTime = LocalDateTime.now();

		contact.setId(UniqueId);
		contact.setCreatedAt(localDateTime);
		contact.setUpdatedAt(localDateTime);
		contact.setTenant(tenantEmail); //better to set to primary key 🐛

		Contacts savedContact = contactsRepo.save(contact);
		return savedContact;
	}

	public List<Contacts> getAllContactsWithCurrentTenant(){
		String tenantEmail = getNameFromSecurityContext();
		List<Contacts> contacts = findContactsByTenant(tenantEmail);
		logger.info("Tenant of email {} accessed their contacts", tenantEmail);
		return  contacts;
	}

	public boolean deleteContactByEmailForCurrentTenant(String contactEmail){
		String tenantEmail = getNameFromSecurityContext();
		Optional<Contacts> contact = contactsRepo.findByEmailAndTenant(contactEmail, tenantEmail);
		boolean contactEmpty = contact.isEmpty();
		if(contactEmpty){
			logger.warn("Tried to delete contact with email {} owned by tenant of email {}, but there was no such contact", contactEmail, tenantEmail);
			return false;
		}
		contactsRepo.delete(contact.get());
		logger.info("Contact of email {} owned by tenant of email {} successfully deleted", contactEmail, tenantEmail);
		return true;

	}

	private Contacts findContactsByEmailAndTenant(String contactEmail, String tenantEmail){
		//tenant cant have multiple contacts with the same email
		Optional<Contacts> contacts = contactsRepo.findByEmailAndTenant(contactEmail, tenantEmail);
		boolean contactsEmpty = contacts.isEmpty();
		if(contactsEmpty){
			logger.warn("Tried to get contact of email {} for tenant of email {}, but there was no such contact", contactEmail, tenantEmail);
			return null;
		}
		return contacts.get();
	}

	private List<Contacts> findContactsByTenant(String tenantEmail){
		Optional<List<Contacts>> contacts = contactsRepo.findByTenant(tenantEmail);
		if(contacts.isEmpty()){
			logger.warn("Tenant of email {} tried to access contacts but was empty", tenantEmail);
			return null;
		}
		return contacts.get();
	}

	private String getNameFromSecurityContext(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private String generatedIdFrom(String string){
		return UUID.nameUUIDFromBytes(string.getBytes()).toString();
	}
}
