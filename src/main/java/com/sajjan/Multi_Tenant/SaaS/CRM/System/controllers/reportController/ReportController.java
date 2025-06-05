package com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.reportController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.controllers.contactsController.wrapperClasses.StringWrapper;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.Reports;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.ReportsService;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.controllerDtos.ReportDetails;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.ReportMakerService;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.ReportsPdf;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.makePdfFile.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	ReportsService reportsService;

	@Autowired
	ReportMakerService reportMakerService;

	@Autowired
	PdfGeneratorService pdfGeneratorService;

	@GetMapping("/getReportHtml")
	public String getReportHtml(Model model, @RequestBody StringWrapper stringWrapper) {

		String forProject = stringWrapper.getData();
		Reports reports = reportsService.findReportForProject(forProject);
		ReportsPdf pdf = reportMakerService.makeReport(reports);
		List<Map<String, String>> contributions = pdf.getListMapOfContributions();
		List<Map<String, String>> contributorDetails = pdf.getListMapOfContributorDetails();
		Map<String, String> notes = pdf.getMapOfMgrNotes();
		Map<String, String> title = pdf.getMapOfTitle();
		Map<String, String> date = pdf.getMapOfDate();
		Map<String, String> mgrDetails = pdf.getMapOfMgrDetails();


		model.addAttribute("contributions", contributions);
		model.addAttribute("contributorDetails", contributorDetails);
		model.addAttribute("notes", notes);
		model.addAttribute("title", title);
		model.addAttribute("date", date);
		model.addAttribute("author", mgrDetails);

		//

		return "report"; // Resolves to templates/report.html
	}

	@PostMapping("/getReportPdf")
	public ResponseEntity<byte[]> getReportPdf(@RequestBody StringWrapper stringWrapper) {
		String forProject = stringWrapper.getData();
		Reports reports = reportsService.findReportForProject(forProject);

		// Create a map to hold all model attributes
		Map<String, Object> model = new HashMap<>();
		ReportsPdf pdf = reportMakerService.makeReport(reports);

		model.put("contributions", pdf.getListMapOfContributions());
		model.put("contributorDetails", pdf.getListMapOfContributorDetails());
		model.put("notes", pdf.getMapOfMgrNotes());
		model.put("title", pdf.getMapOfTitle());
		model.put("date", pdf.getMapOfDate());
		model.put("author", pdf.getMapOfMgrDetails());

		// Generate PDF from template
		byte[] pdfContents = pdfGeneratorService.generatePdfFromTemplate("report", model);

		// Set up HTTP headers for PDF download
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "report.pdf");

		// Return the PDF as a response
		return ResponseEntity.ok()
				.headers(headers)
				.body(pdfContents);
	}

	@GetMapping("/getReportByProjectName") //duplicate end point names crash the application
	public ResponseEntity getReportJson(@RequestBody ReportDetails reportDetails) throws JsonProcessingException {
		String projectName = reportDetails.getForProject();
		String reports = reportsService.reportJsonByName(projectName);
		return ResponseEntity.ok(reports);
	}

	@GetMapping("/getMyReports")
	public ResponseEntity geMyReports(){
		List<Reports> reports = reportsService.getAllUserReports();
		if(reports.size()==0){
			return ResponseEntity.badRequest().body("There were no reports for user");
		}
		return ResponseEntity.ok(reports);
	}
	@PostMapping("/makeReport") //duplicate end point names crash the application
	public ResponseEntity getLastWorkedOnTaskForTenant(@RequestBody  ReportDetails reportDetails){
		String projectName = reportDetails.getForProject();
		String title = reportDetails.getTitle();
		String notes = reportDetails.getNotes();
		boolean success = reportsService.successfullyMakeNewReport(title, notes, projectName);
		ResponseEntity response = success ?
				ResponseEntity.ok("Successfully created new report")
				: ResponseEntity.badRequest().body("Failed to create new report");
		return response;
	}

}
