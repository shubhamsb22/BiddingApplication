package no.kobler.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.Redisson;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import no.kobler.api.BidRequest;
import no.kobler.core.Campaign;
import no.kobler.db.BidDAO;
import no.kobler.db.CampaignDAO;
import no.kobler.exception.GenericException;
import no.kobler.redisconfiguration.RedissonManaged;

@ExtendWith(DropwizardExtensionsSupport.class)
public class BidServiceTest {
	
	private static final BidDAO BID_DAO = mock(BidDAO.class);
	private static final CampaignDAO CAMPAIGN_DAO = mock(CampaignDAO.class);
	private BidService bidService = new BidService(BID_DAO,CAMPAIGN_DAO);
	RedissonClient redissonClient = Redisson.create();
  	RedissonManaged redissonManaged = new RedissonManaged(redissonClient);
	RMapCache<Integer, Integer> cacheMap = redissonClient.getMapCache("temp");

	private BidRequest bid;
	private List<Integer> campaignlist;
	private String[] s=new String[1];
	private Campaign campaign=new Campaign();

	@BeforeEach
	public void setUp() {
		
		campaignlist=new ArrayList<Integer>();
		
		bid = new BidRequest();
		bid.setBidId(1);
		
		s[0]="Kobler";
		bid.setKeywords(s);

		campaign.setBudget(5);
		campaign.setId(1);
		campaign.setSpending(1);
		campaign.setName("Sample");
		
		String[] al=new String[] {"Kobler","Contextual"};
		campaign.setKeywords(al);
	}

	@AfterEach
	public void tearDown() {
		reset(BID_DAO);
	}

	@Test
	public void createBid() throws JsonProcessingException, GenericException {
		
		campaignlist.add(1);

		when(BID_DAO.getAllCampaignIDbyKeywords(s)).thenReturn(campaignlist);
		when(CAMPAIGN_DAO.getCampaign(1)).thenReturn(campaign);
		doNothing().when(BID_DAO).updateCampaignTable(any(Double.class), any(Integer.class));

		final Boolean response = bidService.processBid(bid, cacheMap);

		assertThat(response).isEqualTo(true);
	}

	@Test
	public void createBid_Negative_Test() throws JsonProcessingException, GenericException {

		when(BID_DAO.getAllCampaignIDbyKeywords(s)).thenReturn(campaignlist);
		when(CAMPAIGN_DAO.getCampaign(0)).thenReturn(campaign);
		doNothing().when(BID_DAO).updateCampaignTable(any(Double.class), any(Integer.class));

		final Boolean response = bidService.processBid(bid, cacheMap);

		assertThat(response).isEqualTo(false);
	}

}