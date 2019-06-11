package no.kobler.services;

import java.util.List;
import java.util.Random;

import no.kobler.api.BidRequest;
import no.kobler.core.Campaign;
import no.kobler.db.BidDAO;
import no.kobler.db.CampaignDAO;

public class BidService {
	
	private BidDAO bidDAO;
	private CampaignDAO campaignDAO;
	
	public BidService(BidDAO bidDAO, CampaignDAO campaignDAO) {
		super();
		this.bidDAO = bidDAO;
		this.campaignDAO = campaignDAO;
	}
	
	public Boolean processBid(BidRequest bidRequest) {
		List<Integer> campaign=bidDAO.getAllCampaignIDbyKeywords(bidRequest.getKeywords());
    	
    	if(campaign.isEmpty()){
    		return false;
    	}
    	
    	Random rand = new Random();
        Integer randomElement = campaign.get(rand.nextInt(campaign.size()));
        Campaign targetcampaign =campaignDAO.getCampaign(randomElement.intValue());
        
        bidDAO.updateCampaignTable(targetcampaign.getSpending()+1,randomElement);
        return true;
	}
}
