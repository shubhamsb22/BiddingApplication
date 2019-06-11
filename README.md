# BiddingApplication
A Dropwizard web application to create keyword based advertising campaigns and activate campaigns in response to real time bidding requests.

# Introduction
The Application demonstrates a sample application that creates an API to create campaigns and bid against them. The Application uses an im-memory H2 database to store session specific data. Hibernate libraries are used in addition to smoothen the interaction with the database.

A brief description of the classes in the application:

* CampaignDAO is the Data Access Object for mapping the Campaigns from an object-oriented programming model with the database. 

* BidsDAO is the Data Access Object for mapping the Bids from an object-oriented programming model with the database. 

* BidsResource and CampaignResource are the resources to create the rest API for Bids and Campaigns Correspondingly.

# Running the Application

* To Build the Application , please run the following commands:

        cd BiddingApplication
        mvn package

* To Run the Application, please run the following commands:

        java -jar target/biddingapplication-0.0.1-SNAPSHOT.jar server config.yml

* To test the API, please use the following commands:

        