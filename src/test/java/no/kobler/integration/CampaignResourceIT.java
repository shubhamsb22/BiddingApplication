package no.kobler.integration;


import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import no.kobler.BiddingAppApplication;
import no.kobler.BiddingAppConfiguration;
import no.kobler.api.CampaignRequest;
import no.kobler.api.CampaignResponse;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CampaignResourceIT {

	static String TMP_FILE = createTempFile();
	static String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-example.yml");

	
	private static DropwizardAppExtension<BiddingAppConfiguration> RULE = new DropwizardAppExtension<>(
			BiddingAppApplication.class, CONFIG_PATH, 
			ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));


	private static CampaignRequest req;

	private static String createTempFile() { 
		
		 try {
			 String s=File.createTempFile("test-example", null).getAbsolutePath();
			 System.out.println(s);
	            return s;
	        } catch (IOException e) {
	            throw new IllegalStateException(e);
	        }
	}



	@BeforeAll
	public static void migrateDb() throws Exception {
		RULE.getApplication().run("server", CONFIG_PATH);

		req  = new CampaignRequest();
		req.setBudget(10);
		List<String> keywords = new ArrayList<>();
		keywords.add("kobler");
		req.setKeywords(keywords);
		req.setName("test");
	}

	@Test
	public void testPostCampaign() throws Exception {

		final Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/campaigns")
				.request()
				.post(Entity.entity(req, MediaType.APPLICATION_JSON_TYPE));

		String responseBody = response.readEntity(String.class);
		assertEquals("Location: /campaigns/1", responseBody);
	}


	@Test
	public void testGetCampagin() throws Exception {

		final Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/campaigns/1")
				.request()
				.get(Response.class);

		CampaignResponse responseBody = response.readEntity(CampaignResponse.class);
		assertEquals(req.getBudget(), responseBody.getBudget());
		assertEquals(req.getName(), responseBody.getName());
		assertEquals(0, responseBody.getSpending());
	}

	@Test
	public void testGetCampagin_Negative() throws Exception {

		final Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/campaigns/0")
				.request()
				.get(Response.class);

		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
	}


	
	@Test
	public void testLogFileWritten() throws IOException {
		
		final Path log = Paths.get("./logs/application.log"); assertThat(log).exists();
	  final String actual = new String(Files.readAllBytes(log), UTF_8);
	  assertThat(actual).contains("0.0.0.0:" + RULE.getLocalPort()); }
	 }
