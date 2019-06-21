package no.kobler.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import no.kobler.core.Campaign;

public interface BidDAO{
	
	@SqlQuery("select * from Campaigns where exists( select * from Campaigns " + 
			"locate(:keywords,' '||Campaigns.keywords||' ' ) <> 0);")
    List<Campaign> getBid(@Bind("keywords") String keywords);
	
	@SqlQuery("select campaign_id from Keywords,Campaigns where keyword in (:keywords) and budget  > spending and campaign_id=Campaigns.id")
    List<Integer> getAllCampaignIDbyKeywords(@Bind("keywords") String[] keywords);
	
	@SqlUpdate("UPDATE Campaigns SET spending = :spending where id =:id")
    void updateCampaignTable(@Bind("spending") double spending,@Bind("id") int id);
	
}
