package no.kobler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BidRequest {
	@JsonProperty
	private int bidId;
	
	@JsonProperty
	private String[] keywords;

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

}
