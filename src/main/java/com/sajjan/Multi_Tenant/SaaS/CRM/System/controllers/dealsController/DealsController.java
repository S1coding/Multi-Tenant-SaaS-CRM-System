package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.dealsController;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.contactsController.wrapperClasses.StringWrapper;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.dealsController.wrapperClasses.DealsDetails;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.deal.Deals;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.deals.DealsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deal")
public class DealsController {

	@Autowired
	DealsService dealsService;

	@PostMapping("/createDeal")
	public ResponseEntity createDealForCurrentTenant(@RequestBody DealsDetails dealsDetails){


		boolean success = dealsService.successfulCreateDealForCurrentTenant(dealsDetails);
		ResponseEntity response =success ?
				ResponseEntity.ok("New deal successfully created")
				: ResponseEntity.badRequest().body("Illegal deal details provided or deal already exists");

		return response;
	}

	@PostMapping("/updateDeal")
	public ResponseEntity updateDealForCurrentTenant(@RequestBody  DealsDetails dealsDetails){
		boolean success = dealsService.successfulUpdateForCurrentTenant(dealsDetails);
		ResponseEntity response = success ?
				ResponseEntity.ok("Deal successfully updated")
				: ResponseEntity.badRequest().body("Update details failed, credentials were  probably invalid or illegal");
		return response;
	}

	@GetMapping("/getMyDeals")
	public ResponseEntity getTenantDeals(){
		List<Deals> dealsList = dealsService.getAllDealsForCurrentTenant();
		return ResponseEntity.ok(dealsList);
	}

	@PostMapping("/deleteDealMadeTo")
	public ResponseEntity deleteDealMadeTo(@RequestBody  StringWrapper email){
		boolean success = dealsService.successfullyDeleteDealWithCurrentTenant(email);
		ResponseEntity response = success ?
				ResponseEntity.ok("Successfully deleted deal")
				: ResponseEntity.badRequest().body("Failed to delete deal, it might not exist");
		return response;
	}
}
