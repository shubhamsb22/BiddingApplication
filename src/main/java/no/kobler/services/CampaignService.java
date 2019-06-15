package no.kobler.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.kobler.api.CampaignRequest;
import no.kobler.core.Campaign;
import no.kobler.db.CampaignDAO;
import no.kobler.exception.GenericException;

public class CampaignService {
	
	Logger log = LoggerFactory.getLogger(CampaignService.class);
	private CampaignDAO campaignDAO;

	public CampaignService(CampaignDAO campaignDAO) {
		this.campaignDAO = campaignDAO;
	}
	
	public Integer processCampaign(CampaignRequest campaignRequest) throws GenericException {
		
		Campaign campaign=new Campaign(campaignRequest);
		int x=0;
		
		try {
    	x = campaignDAO.addCampaign(campaign);
    	campaign.setId(x);

    	for(String i:campaignRequest.getKeywords()) 
    		campaignDAO.addKeywords(i, x);
		}
		catch(Exception e)
		{
			log.error("Exception occured while processing campaign with name : {} "
					+ "with reason: {}", campaign.getName(), e);
			throw new GenericException("Something went wrong with database: " +e.getMessage());
		}
    		
    	return x;
	}
	
	public Campaign fetchCampaign(int id) throws GenericException {
		
		try
		{
		return campaignDAO.getCampaign(id);
		}
		catch(Exception e)
		{
			log.error("Exception occured while fetching campaign id: {} with reason: {}", id, e);
			throw new GenericException("Something went wrong with database: " +e.getMessage());
		}
	}
}
