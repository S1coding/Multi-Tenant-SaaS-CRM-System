package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.deals;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.contactsController.wrapperClasses.StringWrapper;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.dealsController.wrapperClasses.DealsDetails;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.deal.Deals;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.deal.DealsRepo;
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
public class DealsService {

	@Autowired
	private DealsRepo dealsRepo;

	private Logger logger = LoggerFactory.getLogger(DealsService.class);

	//TODO: Important to denote that the deal id is made using the madeTo and madeFor emails,
	// so each tenant can only have one deal per customer. Might have to remake if a tenant can make multiple deals per customer
	// by modifying how the id is generated.
	public boolean successfulCreateDealForCurrentTenant(DealsDetails dealsDetails){

		String tenantEmail = getUsernameFromSecurityContext();
		dealsDetails.setMadeBy(tenantEmail); //ensures integrity, just for safety not sure if its actually needed

		if(isDealsDetailNull(dealsDetails)){
			logger.warn("Tried to use a null or illegal values for DealsDetail to create a new deal for Deal for tenant '{}'", tenantEmail);
			return false;
		}
		String madeTo = dealsDetails.getMadeTo();
		String madeBy = dealsDetails.getMadeBy();
		if(checkDealExists(dealsDetails)){
			logger.warn("Tried to create a deal made to '{}' by '{}', but it already exists", madeTo, madeBy);
			return false;
		}

		Deals newDeal = new Deals();
		LocalDateTime localDateTime = LocalDateTime.now();
		String dealId = generatedIdFrom(madeTo+madeBy);

		newDeal.setId(dealId);
		newDeal.updateDetailsToDeal(dealsDetails);
		newDeal.setCreatedAt(localDateTime);
		newDeal.setUpdatedAt(localDateTime);

		Deals savedDeal = dealsRepo.save(newDeal);
		logger.info("Successfully saved deal made to {} by {}", savedDeal.getMadeTo(), savedDeal.getMadeBy());
		return true;
	}

	public boolean successfulUpdateForCurrentTenant(DealsDetails updatedDetails) {

		final String tenantEmail = getUsernameFromSecurityContext(); //should use final for clarity i suppose
		updatedDetails.setMadeBy(tenantEmail); // enforce ownership

		// Validate input
		if (isDealsDetailNull(updatedDetails)) {
			logger.warn("Attempted to update a deal with null or empty fields for tenant '{}'", tenantEmail);
			return false;
		}

		final String recipient = updatedDetails.getMadeTo();
		Optional<Deals> existingDealOpt = dealsRepo.findByMadeToAndMadeBy(recipient, tenantEmail);

		if (existingDealOpt.isEmpty()) {
			logger.warn("No existing deal found to update for recipient '{}' and tenant '{}'", recipient, tenantEmail);
			return false;
		}

		Deals existingDeal = existingDealOpt.get();
		existingDeal.updateDetailsToDeal(updatedDetails);

		dealsRepo.save(existingDeal);

		logger.info("Successfully updated deal madeTo '{}' for tenant '{}'", recipient, tenantEmail);
		return true;
	}

	public List<Deals> getAllDealsForCurrentTenant(){
		String email = getUsernameFromSecurityContext();
		Optional<List<Deals>> deals = dealsRepo.findByMadeBy(email);
		if(deals.isEmpty()){
			logger.warn("Tried to retrieve deals made by '{}', but it was null", email);
			return null;
		}
		return deals.get();
	}


	public boolean successfullyDeleteDealWithCurrentTenant(StringWrapper dealMadeTo){
		final String tenantEmail = getUsernameFromSecurityContext();
		final String contactEmail = dealMadeTo.getData();
		Optional<Deals> deal = dealsRepo.findByMadeToAndMadeBy(contactEmail, tenantEmail);
		logger.info("Found deal made to '{}' made by '{}'", contactEmail, tenantEmail);
		if(deal.isEmpty()){
			logger.info("Tried to delete deal that does not exist");
			return false;
		}
		dealsRepo.delete(deal.get());
		logger.info("Successfully deleted deal made to '{}' made by '{}'", contactEmail, tenantEmail);
		return true;
	}

	private boolean checkDealExists(DealsDetails dealsDetails){
		String madeTo = dealsDetails.getMadeTo();
		String madeBy = dealsDetails.getMadeBy();
		Optional<Deals> dealIfExists = dealsRepo.findByMadeToAndMadeBy(madeTo, madeBy);
		logger.info("Checking if deal with madeTo '{}' and madeBy '{}' already exists", madeTo, madeBy);
		boolean exists = dealIfExists.isEmpty() ?
				false : true;
		return  exists;
	}

	private boolean isDealsDetailNull(DealsDetails dealsDetails){
		boolean isDealDetailsNull = dealsDetails==null;
		boolean isMadeToNull = dealsDetails.getMadeTo().isEmpty() || dealsDetails.getMadeTo() == null;
		boolean isMadeByNull = dealsDetails.getMadeBy().isEmpty() || dealsDetails.getMadeBy() == null;
		if(isDealDetailsNull || isMadeByNull || isMadeToNull){
			return true;
		}
		return false;
	}

	private String getUsernameFromSecurityContext(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private String generatedIdFrom(String string){
		return UUID.nameUUIDFromBytes(string.getBytes()).toString();
	}


	//TODO: make a separate class for generateFromId function

}
