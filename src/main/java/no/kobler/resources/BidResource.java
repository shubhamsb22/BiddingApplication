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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import no.kobler.api.BidRequest;
import no.kobler.api.BidResponse;
import no.kobler.exception.GenericException;
import no.kobler.services.BidService;

@Path("/bids")
@Api(value = "/bids", description = "Operations about bids")
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
	@ApiOperation(
			value = "bid on a campaign ",
			notes = "Returns a bid ",
			response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 502, message = "Something went wrong"),
			@ApiResponse(code = 204, message = "No campaign found")})
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
