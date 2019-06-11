package no.kobler;

import org.skife.jdbi.v2.DBI;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import no.kobler.db.BidDAO;
import no.kobler.db.CampaignDAO;
import no.kobler.resources.BidResource;
import no.kobler.resources.CampaignResource;
import no.kobler.services.BidService;
import no.kobler.services.CampaignService;


public class biddingappApplication extends Application<biddingappConfiguration> {

    public static void main(final String[] args) throws Exception {
        new biddingappApplication().run(args);
    }
    @Override
    public void initialize(Bootstrap<biddingappConfiguration> bootstrap) {
    }
    
    @Override
    public String getName() {
        return "biddingapp";
    }
    
    @Override
    public void run(biddingappConfiguration conf, Environment env) throws ClassNotFoundException {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(env, conf.getDataSourceFactory(),"h2");
        final CampaignDAO campaignDAO = jdbi.onDemand(CampaignDAO.class);
        campaignDAO.createCampaignTable();
        campaignDAO.createKeywordTable();
        final BidDAO bidDAO = jdbi.onDemand(BidDAO.class);
        
        env.jersey().register(new CampaignResource(new CampaignService(campaignDAO)));
        env.jersey().register(new BidResource(new BidService(bidDAO, campaignDAO)));
    }
}
