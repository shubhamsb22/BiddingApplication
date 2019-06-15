package no.kobler.resources;

import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.redisson.api.RMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.kobler.api.BidRequest;
import no.kobler.api.BidResponse;
import no.kobler.exception.GenericException;
import no.kobler.services.BidService;

@Path("/bids")
public class BidResource {
	
	Logger log = LoggerFactory.getLogger(BidResource.class);
	private BidService bidService;
	private RMapCache<Integer, Integer> cacheMap;
    
	public BidResource(BidService bidService, RMapCache<Integer, Integer> cacheMap) {
        this.bidService = bidService;
        this.cacheMap = cacheMap;
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response processBid(BidRequest bidRequest) throws InterruptedException {
    	
    	Boolean status;
		try {
			status = bidService.processBid(bidRequest, cacheMap);
		} catch (GenericException e) {
			log.error("Exception occured with reason: {}", e);
			return Response.status(Response.Status.BAD_GATEWAY).build();
		}
    	if(status)
        return Response.ok(new BidResponse(bidRequest.getBidId(),BigDecimal.ONE),MediaType.APPLICATION_JSON).build();
    	
    	else {
    		return Response.status(Response.Status.NO_CONTENT).build();
    	}
    }
}
