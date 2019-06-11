package no.kobler.api;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.kobler.core.Campaign;

public class CampaignResponse {
	
	public CampaignResponse() {
	}

	public CampaignResponse(Campaign campaign) {
		this.name = campaign.getName();
		this.budget = campaign.getBudget();
		this.spending = campaign.getSpending();
		List<String> list = Arrays.asList(campaign.getKeywords());
		this.keywords = list;
	}
	@JsonProperty
	private String name;
	
	@JsonProperty
	private double budget;
	
	@JsonProperty
	private List<String> keywords;
	
	@JsonProperty
	private double spending;

	public double getSpending() {
		return spending;
	}

	public void setSpending(double spending) {
		this.spending = spending;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

}
