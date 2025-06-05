package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.Reports;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.ReportsRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.TasksRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.TenantsRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.ReportMakerService;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.ReportsPdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReportsService {

	@Autowired
	private ReportsRepo reportsRepo;

	@Autowired
	private ReportMakerService reportMakerService;

	@Autowired
	private TenantsRepo tenantsRepo;

	@Autowired
	private TasksRepo tasksRepo;

	private final Logger logger = LoggerFactory.getLogger(ReportsService.class);

	public String reportJson(Reports report) throws JsonProcessingException {
		if(report == null){
			logger.warn("Null report given");
			return null;
		}
		ReportsPdf pdf = reportMakerService.makeReport(report);
		return pdf.convertToJson();
	}

	public List<Reports> getAllUserReports(){
		String email = getNameFromSecurityContext();
		List<Reports> reports = reportsRepo.findByMadeBy(email);
		return reports;
	}

	public String reportJsonByName(String projectName) throws JsonProcessingException {
		String email = getNameFromSecurityContext();
		Reports report = reportsRepo.findByForProjectAndMadeBy(projectName, email).get();
		return reportJson(report);
	}

	public Reports findReportForProject(String forProject){
		String email = getNameFromSecurityContext();
		Optional<Reports> reportsOpt = reportsRepo.findByForProjectAndMadeBy(forProject, email);
		if(reportsOpt.isEmpty()){
			logger.warn("Tried to get report for '{}' made by '{}' but found nothing", forProject, email);
			return null;
		}
		return reportsOpt.get();
	}

	public boolean successfullyMakeNewReport(String title, String notes, String projectName){
		String email = getNameFromSecurityContext();
		Optional<Reports> reportsOptional = reportsRepo.findByForProjectAndMadeBy(projectName, email);
		if(reportsOptional.isPresent()){
			logger.warn("Tried to make a report by manager '{}' for project '{}', but report already exists", email, projectName);
			return false;
		}
		String id = generatedIdFrom(email+projectName);
		LocalDateTime dateTime = LocalDateTime.now();
		Reports report = Reports.builder()
				.forProject(projectName)
				.id(id).madeBy(email)
				.notes(notes)
				.title(title)
				.createdAt(dateTime)
				.updatedAt(dateTime)
				.build();
		reportsRepo.save(report);
		logger.info("Saved report '{}' to table", report);
		return true;
	}

	private String getNameFromSecurityContext(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private String generatedIdFrom(String string){
		return UUID.nameUUIDFromBytes(string.getBytes()).toString();
	}


	//get tenant details
	//get their deals, customers and tasks
	//make some sort of structured document from them

}
