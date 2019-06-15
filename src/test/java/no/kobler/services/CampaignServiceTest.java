package no.kobler.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import no.kobler.api.CampaignRequest;
import no.kobler.core.Campaign;
import no.kobler.db.CampaignDAO;
import no.kobler.exception.GenericException;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CampaignServiceTest {
	
	private static final CampaignDAO CAMPAIGN_DAO = mock(CampaignDAO.class);
	private CampaignService campaignService = new CampaignService(CAMPAIGN_DAO);
   
    private CampaignRequest campaignRequest;
	private Campaign campaign=new Campaign();
	
    @BeforeEach
    public void setUp() {
		
		String[] al=new String[] {"Kobler","Contextual"};
		  
        campaignRequest = new CampaignRequest();
        campaignRequest.setBudget(5);
		campaignRequest.setName("Sample");
		
        List<String> allist= new ArrayList<String>();
        allist.add("Kobler");
        campaignRequest.setKeywords(allist);
    	
    	campaign.setBudget(5);
    	campaign.setId(1);
    	campaign.setSpending(1);
    	campaign.setName("Sample");
    	campaign.setKeywords(al);
    }
    
    @Test
    public void createCampaign() throws Exception {

		when(CAMPAIGN_DAO.addCampaign(any(Campaign.class))).thenReturn(0);
		when(CAMPAIGN_DAO.addKeywords(any(String.class),any(Integer.class))).thenReturn(0);
	
		final Integer response = campaignService.processCampaign(campaignRequest);
		assertThat(response).isEqualTo(0);
    }
    
    @Test
    public void createCampaign_Negative() throws Exception {

		when(CAMPAIGN_DAO.addCampaign(any(Campaign.class))).thenThrow(Exception.class);
		when(CAMPAIGN_DAO.addKeywords(any(String.class),any(Integer.class))).thenReturn(0);
		
		Assertions.assertThrows(GenericException.class, () -> campaignService.processCampaign(campaignRequest));
    }
	
	 @Test
    public void fetchCampaign() throws JsonProcessingException, GenericException {
    	
		when(CAMPAIGN_DAO.getCampaign(0)).thenReturn(campaign);
		
		final Campaign response = campaignService.fetchCampaign(0);
		
        assertThat(response.getName()).isEqualTo(campaign.getName());
		assertThat(response.getBudget()).isEqualTo(campaign.getBudget());
		
    }
	 @Test
	    public void fetchCampaign_negative() throws JsonProcessingException, GenericException {
	    	
			when(CAMPAIGN_DAO.getCampaign(0)).thenReturn(null);
			
			final Campaign response = campaignService.fetchCampaign(0);

	        assertThat(response).isEqualTo(null);
	    }
}