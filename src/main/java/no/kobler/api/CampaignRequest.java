package no.kobler.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CampaignRequest {
	@JsonProperty
	private String name;
	
	@JsonProperty
	private float budget;
	
	@JsonProperty
	private List<String> keywords;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getBudget() {
		return budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
}
