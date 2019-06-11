package no.kobler.api;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BidResponse {
	
	public BidResponse() {
	}

	public BidResponse(int bidId, BigDecimal bidAmount) {
		super();
		this.bidId = bidId;
		this.bidAmount = bidAmount;
	}

	@JsonProperty
	private int bidId;
	
	@JsonProperty
	private BigDecimal bidAmount;

	public BigDecimal getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(BigDecimal bidAmount) {
		this.bidAmount = bidAmount;
	}

	public int getBidId() {
		return bidId;
	}

	
	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

}
