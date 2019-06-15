package no.kobler.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.Redisson;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import no.kobler.api.BidRequest;
import no.kobler.api.BidResponse;
import no.kobler.exception.GenericException;
import no.kobler.redisconfiguration.RedissonManaged;
import no.kobler.services.BidService;

@ExtendWith(DropwizardExtensionsSupport.class)
public class BidResourceTest {
	private static final BidService BID_SERVICE = mock(BidService.class);
	private static final RedissonClient redissonClient = Redisson.create();
  	RedissonManaged redissonManaged = new RedissonManaged(redissonClient);
	private static final RMapCache<Integer, Integer> cacheMap = redissonClient.getMapCache("temp");

	public static final ResourceExtension RESOURCES = ResourceExtension.builder()
			.addResource(new BidResource(BID_SERVICE, cacheMap))
			.build();

	private BidRequest bid;
	String[] s=new String[1];
	BidResponse bidResponse = new BidResponse(1,BigDecimal.ZERO);

	@BeforeEach
	public void setUp() {
		
		bid = new BidRequest();
		bid.setBidId(1);
		
		s[0]="Kobler";
		bid.setKeywords(s);

	}

	@AfterEach
	public void tearDown() {
		reset(BID_SERVICE);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createBid() throws JsonProcessingException, GenericException {

		when(BID_SERVICE.processBid(any(BidRequest.class), any(RMapCache.class))).thenReturn(true);

		final Response response = RESOURCES.target("/bids")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(bid, MediaType.APPLICATION_JSON_TYPE));

		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		BidResponse bidResponse=response.readEntity(BidResponse.class);
        assertThat(bidResponse.getBidId()).isEqualTo(bid.getBidId());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createBid_Negative_Test() throws JsonProcessingException, GenericException {

		when(BID_SERVICE.processBid(any(BidRequest.class), any(RMapCache.class))).thenReturn(false);

		final Response response = RESOURCES.target("/bids")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(bid, MediaType.APPLICATION_JSON_TYPE));

		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
	}

}