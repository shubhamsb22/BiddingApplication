package no.kobler.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

import no.kobler.api.CampaignRequest;
import no.kobler.api.CampaignResponse;
import no.kobler.core.Campaign;
import no.kobler.exception.GenericException;
import no.kobler.services.CampaignService;

@Path("/campaigns")
public class CampaignResource {

	Logger log = LoggerFactory.getLogger(CampaignResource.class);
	private CampaignService campaignService;


	public CampaignResource(CampaignService campaignService) {
		this.campaignService = campaignService;
	}


	@POST
	@Consumes("application/json")
	@Produces("text/xml")
	@Timed
	public Response processCampaign(CampaignRequest campaignRequest) {

		log.info("received request to add campaign with name: {}", campaignRequest.getName());
		Integer campaignId = null;
		try {
			campaignId = campaignService.processCampaign(campaignRequest);
		} catch (GenericException e) {
			log.error("Exception occured with reason: {}", e);
			return Response.status(Response.Status.BAD_GATEWAY).build();
		}
		return Response.ok("Location: /campaigns/"+campaignId,MediaType.TEXT_XML).build();
	}

	@Path("/{id}")
	@GET
	@Produces("application/json")
	public Response fetchCampaign(@PathParam("id") int id)
	{
		Campaign campaign = null;
		try
		{
			campaign = campaignService.fetchCampaign(id);
		} catch (GenericException e) {
			log.error("Exception occured with reason: {}", e);
			return Response.status(Response.Status.BAD_GATEWAY).build();
		}

		if(null!=campaign) {
			return Response.ok(new CampaignResponse(campaign),MediaType.APPLICATION_JSON).build();
		}
		else {
			return Response.status(Response.Status.NO_CONTENT).build();
		}

	}
}
