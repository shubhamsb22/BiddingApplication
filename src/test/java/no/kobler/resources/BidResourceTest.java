package no.kobler.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import no.kobler.api.BidRequest;
import no.kobler.api.BidResponse;
import no.kobler.services.BidService;

@ExtendWith(DropwizardExtensionsSupport.class)
public class BidResourceTest {
	private static final BidService BID_SERVICE = mock(BidService.class);

	public static final ResourceExtension RESOURCES = ResourceExtension.builder()
			.addResource(new BidResource(BID_SERVICE))
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

	@Test
	public void createBid() throws JsonProcessingException {

		when(BID_SERVICE.processBid(any(BidRequest.class))).thenReturn(true);

		final Response response = RESOURCES.target("/bids")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(bid, MediaType.APPLICATION_JSON_TYPE));

		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		BidResponse bidResponse=response.readEntity(BidResponse.class);
        assertThat(bidResponse.getBidId()).isEqualTo(bid.getBidId());
	}

	@Test
	public void createBid_Negative_Test() throws JsonProcessingException {

		when(BID_SERVICE.processBid(any(BidRequest.class))).thenReturn(false);

		final Response response = RESOURCES.target("/bids")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(bid, MediaType.APPLICATION_JSON_TYPE));

		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
	}

}