package no.kobler.core;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import no.kobler.api.CampaignRequest;

@Entity
public class Campaign {
	public Campaign(CampaignRequest campaignRequest) {
		
		this.name = campaignRequest.getName();
		this.budget = campaignRequest.getBudget();
		
		String[] stringArray = Arrays.copyOf(campaignRequest.getKeywords().toArray(), campaignRequest.getKeywords().toArray().length, String[].class);
		this.keywords = stringArray;
	}

	public Campaign() {
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private double budget;
	private String[] keywords;
	private double spending;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String[] getKeywords() {
		return keywords;
	}
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	@Override
	public String toString() {
		return "Campaign [id=" + id + ", name=" + name + ", budget=" + budget + ", keywords="
				+ Arrays.toString(keywords) + ", spending=" + spending + "]";
	}
}
