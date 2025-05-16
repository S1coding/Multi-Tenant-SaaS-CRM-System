package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

	@GetMapping("/lastWorkedOnTaskForTenant")
	public ResponseEntity getLastWorkedOnTaskForTenant(){
		return null;
	}

	@GetMapping("/lastWorkedOnTaskForTenant")
	public ResponseEntity getTaskReportForTenant(){
		return null;
	}
}
