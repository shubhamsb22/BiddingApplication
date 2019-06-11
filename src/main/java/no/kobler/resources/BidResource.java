package no.kobler.resources;

import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import no.kobler.api.BidRequest;
import no.kobler.api.BidResponse;
import no.kobler.services.BidService;

@Path("/bids")
public class BidResource {
	
	private BidService bidService;
    
	public BidResource(BidService bidService) {
        this.bidService = bidService;
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response processBid(BidRequest bidRequest) {
    	
    	Boolean status= bidService.processBid(bidRequest);
    	if(status)
        return Response.ok(new BidResponse(bidRequest.getBidId(),BigDecimal.ONE),MediaType.APPLICATION_JSON).build();
    	
    	else {
    		return Response.status(Response.Status.NO_CONTENT).build();
    	}
    }
}
