package no.kobler.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import no.kobler.core.Campaign;

@RegisterMapperFactory(BeanMapperFactory.class)
public interface CampaignDAO {
	
	@SqlUpdate("CREATE TABLE IF NOT EXISTS Campaigns (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255) NOT NULL, budget NUMERIC(8,2) NOT NULL,spending NUMERIC(8,2) NOT NULL,keywords ARRAY NOT NULL, primary key(id))")
    void createCampaignTable();
	
	@SqlUpdate("CREATE TABLE IF NOT EXISTS Keywords (keyword VARCHAR(255) NOT NULL, campaign_id INT NOT NULL,foreign key (campaign_id) references campaigns(id))")
    void createKeywordTable();
	
	@SqlUpdate("INSERT INTO Campaigns (NAME,BUDGET,SPENDING,KEYWORDS) VALUES(:name, :budget, :spending, :keywords)")
    @GetGeneratedKeys
    int addCampaign(@BindBean Campaign employee);
	
	@SqlUpdate("INSERT INTO Keywords (keyword,campaign_id) VALUES(:keywords,:campaign_id)")
    @GetGeneratedKeys
    int addKeywords(@Bind("keywords") String keywords,@Bind("campaign_id") int campaign_id);
	
	@SqlQuery("SELECT * FROM Campaigns WHERE id = :id")
    Campaign getCampaign(@Bind("id") long id);
}
