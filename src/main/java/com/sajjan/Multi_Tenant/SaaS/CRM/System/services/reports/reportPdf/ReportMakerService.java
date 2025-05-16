package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.deal.DealsRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.Reports;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.ReportsRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.contribution.ContributionDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.contributorsDetails.ContributorDetailsDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.managerDetails.DateDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.managerDetails.MgrDetailsDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.managerDetails.MgrNotesDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.managerDetails.TitleDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.Tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.task.TasksRepo;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.TenantsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportMakerService {

	@Autowired
	private ReportsRepo reportsRepo;

	@Autowired
	private TasksRepo tasksRepo;

	@Autowired
	private TenantsRepo tenantsRepo;

	@Autowired
	private DealsRepo dealsRepo;

	private static ObjectMapper objectMapper = new ObjectMapper();

	// List -> Contributor: firstName, LastName, email
	public List<ContributorDetailsDto> projContributorDetails(Reports reports) {
		String projectTitle = reports.getForProject();
		List<Tasks> tasks = tasksRepo.findByTaskProjectAndStatus(projectTitle, "COMPLETE");
		List<ContributorDetailsDto> contributorDetailsDtos = new ArrayList<>();
		for (Tasks task:
		     tasks) {
			/**1. get credentials **/String email = task.getAssignedTo();
			/**2. find tenant by credentials**/Tenants tenant = tenantsRepo.findByEmail(email).get();
			/**3. make a dto of tenant**/ContributorDetailsDto reportContributorDetailsDto =
					new ContributorDetailsDto(tenant);
			/**4. add dto to **/contributionDto.add(reportContributorDetailsDto);
		}
		return contributorDetailsDtos;
	}

	// -> mgr: title, date, email
	public MgrDetailsDto mgrDetails(Reports report)  {
		String title = report.getTitle();
		LocalDateTime date = report.getUpdatedAt();
		String writer = report.getMadeBy();

		Tenants mgrTenant = tenantsRepo.findByEmail(writer).get();
		String firstName = mgrTenant.getFirstName();
		String lastName = mgrTenant.getLastName();

		MgrDetailsDto mgrDetailsDto = new MgrDetailsDto(title, firstName, lastName, writer, date);
		return mgrDetailsDto;
	}

	// List -> Contribution: taskName, date, note, contributor
	public List<ContributionDto> projContributions(Reports report){
		String projectTitle = report.getForProject();
		List<Tasks> tasks = tasksRepo.findByTaskProjectAndStatus(projectTitle, "COMPLETE");
		List<ContributionDto> contributionDtos = new ArrayList<>();
		for(Tasks task: tasks){
			ContributionDto contributionDto = new ContributionDto(task);
			contributionDtos.add(contributionDto);
		}
		return contributionDtos;
	}

	public ReportsPdf makeReport(Reports reports) {
		return new ReportsPdf(
				new TitleDto(reports.getForProject()),
				new DateDto(reports.getUpdatedAt()),
				new MgrNotesDto(reports.getNotes()),
				mgrDetails(reports),
				projContributorDetails(reports),
				projContributions(reports)
		);
	}
}
