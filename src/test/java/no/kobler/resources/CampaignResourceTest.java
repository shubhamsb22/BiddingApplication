package no.kobler.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import no.kobler.api.CampaignRequest;
import no.kobler.api.CampaignResponse;
import no.kobler.core.Campaign;
import no.kobler.exception.GenericException;
import no.kobler.services.CampaignService;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CampaignResourceTest {
	
	private static final CampaignService CAMPAIGN_SERVICE = mock(CampaignService.class);
	
    public static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new CampaignResource(CAMPAIGN_SERVICE))
            .build();
    public static final ResourceExtension RULE = ResourceExtension.builder()
            .addResource(new CampaignResource(CAMPAIGN_SERVICE))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .build();
			
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
    public void createCampaign() throws JsonProcessingException, GenericException {

		when(CAMPAIGN_SERVICE.processCampaign(any(CampaignRequest.class))).thenReturn(0);
		
		final Response response = RESOURCES.target("/campaigns")
                .request(MediaType.TEXT_XML).post(Entity.entity(campaignRequest,MediaType.APPLICATION_JSON));
		

		String x = response.readEntity(String.class);
        assertThat(x).isEqualTo("Location: /campaigns/0");
    }
	
	 @Test
    public void fetchCampaign() throws JsonProcessingException, GenericException {
    	
		when(CAMPAIGN_SERVICE.fetchCampaign(0)).thenReturn(campaign);
		
		Response response = RULE.target("/campaigns/0").request().get(Response.class);
		
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		CampaignResponse campaignResponse=response.readEntity(CampaignResponse.class);
        assertThat(campaignResponse.getName()).isEqualTo(campaign.getName());
		assertThat(campaignResponse.getBudget()).isEqualTo(campaign.getBudget());
		
    }
	 @Test
	    public void fetchCampaign_negative() throws JsonProcessingException, GenericException {
	    	
			when(CAMPAIGN_SERVICE.fetchCampaign(0)).thenReturn(null);
			
			Response response = RULE.target("/campaigns/0").request().get(Response.class);

	        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
	    }
}