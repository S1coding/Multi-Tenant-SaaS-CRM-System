package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.contribution.ContributionDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.contributorsDetails.ContributorDetailsDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.managerDetails.DateDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.managerDetails.MgrDetailsDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.managerDetails.MgrNotesDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.reports.reportPdf.dtos.managerDetails.TitleDto;
import lombok.Data;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;

import java.util.List;

@Data
public class ReportsPdf {

	private TitleDto titleDto;
	private DateDto dateDto;
	private MgrNotesDto managerNotesDto;
	private MgrDetailsDto managerDetailsDto;
	private List<ContributorDetailsDto> contributorTenantDetailsDto;
	private List<ContributionDto> contributionsDto;

	private ObjectMapper objectMapper = new ObjectMapper();
	private ObjectNode root = objectMapper.createObjectNode();

	public ReportsPdf(TitleDto title,
	                  DateDto date,
	                  MgrNotesDto mgrNotes,
	                  MgrDetailsDto mgrDetails,
	                  List<ContributorDetailsDto> contributorDetails,
	                  List<ContributionDto> contributions){
		this.titleDto = title;
		this.dateDto = date;
		this.managerNotesDto = mgrNotes;
		this.managerDetailsDto = mgrDetails;
		this.contributorTenantDetailsDto = contributorDetails;
		this.contributionsDto = contributions;
	}

	public String pdfJson(ReportsPdf report) throws JsonProcessingException {
		root.set("title", objectMapper.valueToTree(report.getTitleDto()));
		root.set("date", objectMapper.valueToTree(report.getDateDto()));
		root.set("managerNotes", objectMapper.valueToTree(report.getManagerNotesDto()));
		root.set("contributorDetails", objectMapper.valueToTree(report.getContributorTenantDetailsDto()));
		root.set("contributions", objectMapper.valueToTree(report.getContributionsDto()));
		return objectMapper.writeValueAsString(root);
	}
}
