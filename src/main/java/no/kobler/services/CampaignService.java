package no.kobler.services;

import no.kobler.api.CampaignRequest;
import no.kobler.core.Campaign;
import no.kobler.db.CampaignDAO;

public class CampaignService {
	private CampaignDAO campaignDAO;

	public CampaignService(CampaignDAO campaignDAO) {
		this.campaignDAO = campaignDAO;
	}
	
	public Integer processCampaign(CampaignRequest campaignRequest) {
		Campaign campaign=new Campaign(campaignRequest);
    	int x= campaignDAO.addCampaign(campaign);
    	
    	for(String i:campaignRequest.getKeywords()) {
    		campaignDAO.addKeywords(i, x);
    	}
    	return x;
	}
	
	public Campaign fetchCampaign(int id) {
		
		return campaignDAO.getCampaign(id);
	}
}
