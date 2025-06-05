package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.dtos.contribution.ContributionDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.dtos.contributorsDetails.ContributorDetailsDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.dtos.managerDetails.DateDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.dtos.managerDetails.MgrDetailsDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.dtos.managerDetails.MgrNotesDto;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.dtos.managerDetails.TitleDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public String convertToJson() throws JsonProcessingException {
		System.out.println("TitleDto: "+ this.getTitleDto().getTitle());
		System.out.println("ManagerNotesDto: "+ this.getManagerNotesDto().getNotes());
		root.set("title", objectMapper.valueToTree(this.getTitleDto()));
		root.set("date", objectMapper.valueToTree(this.getDateDto().getDate().toString()));//jackson cant handle date object
		root.set("managerNotes", objectMapper.valueToTree(this.getManagerNotesDto()));
		root.set("managerDetails", objectMapper.valueToTree(this.getManagerDetailsDto()));
		root.set("contributorDetails", objectMapper.valueToTree(this.getContributorTenantDetailsDto()));
		root.set("contributions", objectMapper.valueToTree(this.getContributionsDto()));
		return objectMapper.writeValueAsString(root);
	}

	public Map<String, String> getMapOfTitle() {
		Map<String, Object> temp = objectMapper.convertValue(titleDto, new TypeReference<Map<String, Object>>() {});

		Map<String, String> result = new HashMap<>();
		for (Map.Entry<String, Object> entry : temp.entrySet()) {
			result.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return result;
	}
	public Map<String, String> getMapOfDate() {
		Map<String, Object> temp = objectMapper.convertValue(dateDto, new TypeReference<Map<String, Object>>() {});

		Map<String, String> result = new HashMap<>();
		for (Map.Entry<String, Object> entry : temp.entrySet()) {
			result.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return result;
	}

	public List<Map<String, String>> getListMapOfContributorDetails() {

		List<Map<String, Object>> tempList = objectMapper.convertValue(
				contributorTenantDetailsDto,
				new TypeReference<List<Map<String, Object>>>() {}
		);

		// Convert each Map<String, Object> → Map<String, String>
		List<Map<String, String>> resultList = new ArrayList<>();

		for (Map<String, Object> map : tempList) {
			Map<String, String> stringMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				stringMap.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
			resultList.add(stringMap);
		}

		return resultList;
	}


	public List<Map<String, String>> getListMapOfContributions() {
		List<Map<String, Object>> tempList = objectMapper.convertValue(
				contributionsDto,
				new TypeReference<List<Map<String, Object>>>(){}
		);

		List<Map<String, String>> resultList = new ArrayList<>();
		for (Map<String, Object> map : tempList) {
			Map<String, String> stringMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				stringMap.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
			resultList.add(stringMap);
		}

		return resultList;
	}

	public Map<String, String> getMapOfMgrDetails() {
		Map<String, Object> temp = objectMapper.convertValue(managerDetailsDto, new TypeReference<Map<String, Object>>() {});

		Map<String, String> result = new HashMap<>();
		for (Map.Entry<String, Object> entry : temp.entrySet()) {
			result.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return result;
	}

	public Map<String, String> getMapOfMgrNotes() {
		Map<String, Object> temp = objectMapper.convertValue(managerNotesDto, new TypeReference<Map<String, Object>>() {});

		Map<String, String> result = new HashMap<>();
		for (Map.Entry<String, Object> entry : temp.entrySet()) {
			result.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return result;
	}
}
