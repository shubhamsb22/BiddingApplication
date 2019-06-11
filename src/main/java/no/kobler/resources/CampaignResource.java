package no.kobler.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import no.kobler.api.CampaignRequest;
import no.kobler.api.CampaignResponse;
import no.kobler.core.Campaign;
import no.kobler.services.CampaignService;

@Path("/campaigns")
public class CampaignResource {
    
    private CampaignService campaignService;
    
    public CampaignResource(CampaignService campaignService) {
        this.campaignService = campaignService;
    }
    
    
    @POST
    @Consumes("application/json")
    @Produces("text/xml")
    public Response processCampaign(CampaignRequest campaignRequest) {
    	
        Integer campaignId = campaignService.processCampaign(campaignRequest);
    	return Response.ok("Location: /campaigns/"+campaignId,MediaType.TEXT_XML).build();
    }
    
    @Path("/{id}")
    @GET
    @Produces("application/json")
    public Response fetchCampaign(@PathParam("id") int id)
    {
    	Campaign campaign= campaignService.fetchCampaign(id);
    	
    	if(null!=campaign) {
    		return Response.ok(new CampaignResponse(campaign),MediaType.APPLICATION_JSON).build();
    	}
    	
    	else {
    		return Response.status(Response.Status.NO_CONTENT).build();
    	}
    	
    }
}
