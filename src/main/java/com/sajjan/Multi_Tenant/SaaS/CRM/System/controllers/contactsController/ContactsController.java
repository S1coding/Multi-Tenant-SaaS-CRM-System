package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.contactsController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.contactsController.wrapperClasses.StringWrapper;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.contact.Contacts;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.contactsController.wrapperClasses.ContactDetails;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.contacts.ContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactsController {

	@Autowired
	private ContactsService contactsService;

	private final Logger logger = LoggerFactory.getLogger(ContactsController.class);

	@PostMapping("/updateContactForTenant")
	public ResponseEntity updateContact(@RequestBody ContactDetails contactDetails) throws JsonMappingException {
		logger.info("Controller endpoint /updateController accessed");
		boolean success = contactsService.updateContactWithCurrentTenant(contactDetails); //throws if update failed
		return success ? ResponseEntity.ok("Successfully updated contact")
				: ResponseEntity.badRequest().body("Failed to update contact");
	}

	@GetMapping("/getContactsByEmail")
	public ResponseEntity updateContactByEmailNotes(@RequestBody StringWrapper emailWrapper){
		String email = emailWrapper.getData();
		logger.info("Retrieving all contacts for a specific schema of email {}", email);
		List<Contacts> contacts = contactsService.getAllSchemaContactsWithEmail(email);
		boolean contactsEmpty = contacts.isEmpty();
		ResponseEntity response =
				contactsEmpty ?
						ResponseEntity.badRequest().body("Attempted to retrieve contacts by email but there were none")
						: ResponseEntity.ok(contacts);
		return response;
	}

	@GetMapping("/getAllContacts")
	public ResponseEntity getAllContacts(){
		logger.info("Retrieving all contacts for a specific schema");
		List<Contacts> contacts = contactsService.getAllSchemaContact();
		boolean contactsEmpty = contacts.isEmpty();
		ResponseEntity response =
				contactsEmpty ?
						ResponseEntity.badRequest().body("Attempted to retrieve contacts but there were none")
						: ResponseEntity.ok(contacts);
		return response;
	}

	@GetMapping("/getContactsForTenant") //TODO: consider changing to /getContactsForCurrentTenant
	public ResponseEntity getContactsForTenant(){
		logger.info("Retrieving tenant from email in SecurityContext");
		List<Contacts> contacts = contactsService.getAllContactsWithCurrentTenant();
		boolean contactsEmpty = contacts == null;
		ResponseEntity response =
				contactsEmpty ?
						ResponseEntity.badRequest().body("Attempted to retrieve contacts but there were none")
						: ResponseEntity.ok(contacts);
		return response;
	}


	@PostMapping("/deleteContactForTenant")
	public ResponseEntity deleteContactForTenant(@RequestBody StringWrapper stringWrapper){
		String contactEmail = stringWrapper.getData();
		logger.info("Deleting contact of email {} for tenant in SecurityContext", contactEmail);
		boolean successfulDeletion = contactsService.deleteContactByEmailForCurrentTenant(contactEmail);
		ResponseEntity response =
				successfulDeletion ?
						ResponseEntity.ok("Contact with email "+contactEmail+" successfully deleted")
						: ResponseEntity.badRequest().body("Tried to delete contact for tenant in SecurityContext that probably didn't exist");
		return response;
	}

	@PostMapping("/addContactForTenant")
	public ResponseEntity addContact(@RequestBody ContactDetails contactDetails) throws JsonMappingException {
		logger.info("Adding contact with email {} to schema table", contactDetails.getEmail());
		Contacts contact = contactsService.createContactWithCurrentTenant(contactDetails);
		boolean contactNull = contact == null;
		ResponseEntity response =
				contactNull ?
						ResponseEntity.badRequest().body("Attempted to add null contact to table")
						: ResponseEntity.ok("Contact successfully added to table "+ contact);
		return  response;
	}
}
