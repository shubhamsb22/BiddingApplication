package no.kobler.services;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.kobler.api.BidRequest;
import no.kobler.core.Campaign;
import no.kobler.db.BidDAO;
import no.kobler.db.CampaignDAO;
import no.kobler.exception.GenericException;

public class BidService {

	Logger log = LoggerFactory.getLogger(BidService.class);
	private BidDAO bidDAO;
	private CampaignDAO campaignDAO;


	public BidService(BidDAO bidDAO, CampaignDAO campaignDAO) {
		super();
		this.bidDAO = bidDAO;
		this.campaignDAO = campaignDAO;
	}

	public Boolean processBid(BidRequest bidRequest, RMapCache<Integer, Integer> cacheMap) throws GenericException {

		boolean status = false;
		Random rand = new Random();
		try
		{
			List<Integer> campaign=bidDAO.getAllCampaignIDbyKeywords(bidRequest.getKeywords());
			if(campaign.isEmpty()){
				return false;
			}
			while(!status)
			{
				if(campaign.isEmpty())
					break;
				Integer randomElement = campaign.get(rand.nextInt(campaign.size()));
				Campaign targetcampaign =campaignDAO.getCampaign(randomElement.intValue());

				status = processBid(cacheMap, targetcampaign);
				campaign.remove(randomElement);
			}
		}
		catch(GenericException e)
		{
			log.error("Exception occured while processig bid request with id: {} "
					+ "with reason: {}", bidRequest.getBidId(), e);
			throw new GenericException(e);
		}
		catch(Exception e)
		{
			log.error("Exception occured while processig bid request with id: {} "
					+ "with reason: {}", bidRequest.getBidId(), e);
			throw new GenericException("Something went wrong with database: " +e.getMessage());
		}
		return status;
	}

	public boolean processBid(RMapCache<Integer, Integer> cacheMap, Campaign campaign) throws GenericException
	{
		try
		{
		int key = campaign.getId();
		if(null != cacheMap.get(key) && cacheMap.get(key) >= 10)
			return false;

		if(null == cacheMap.get(key) || cacheMap.get(key) == 0)
			cacheMap.put(key, 1, 10, TimeUnit.SECONDS);
		else
			incrementCounter(cacheMap, key);

		bidDAO.updateCampaignTable(campaign.getSpending()+1,key);
		}
		catch(Exception e)
		{
			log.error("Something went wrong with error: {}",e);
			throw new GenericException("Something went wrong with error: "+e.getMessage());
		}
		return true;
	}

	public RMapCache<Integer, Integer> incrementCounter(RMapCache<Integer, Integer> cacheMap, int key)
	{
		RLock keyLock = cacheMap.getLock(key);

		keyLock.lock();
		int count = cacheMap.get(key);
		cacheMap.put(1, ++count);
		keyLock.unlock();

		return cacheMap;
	}
}
