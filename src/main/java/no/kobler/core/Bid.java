package no.kobler.core;

import no.kobler.api.BidRequest;

public class Bid {
	
	public Bid(BidRequest bidRequest) {
		
		this.bidId = bidRequest.getBidId();	
		this.keywords = bidRequest.getKeywords();
	}
	
	public Bid() {
	}
	
	private int bidId;
	private double bidAmount;
	private String[] keywords;

	public int getBidId() {
		return bidId;
	}
	public void setBidId(int bidId) {
		this.bidId = bidId;
	}
	public double getBidAmount() {
		return bidAmount;
	}
	public void setBidAmount(double bidAmount) {
		this.bidAmount = bidAmount;
	}
	public String[] getKeywords() {
		return keywords;
	}
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
}
