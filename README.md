# BiddingApplication
A Dropwizard web application to create keyword based advertising campaigns and activate campaigns in response to real time bidding requests.

# Introduction
The Application demonstrates a sample application that creates an API to create campaigns and bid against them. The Application uses an in-memory H2 database to store session specific data. Hibernate libraries are used in addition to smoothen the interaction with the database.

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

* To Run the Application locally, you must install redis 3.0 + and start the redis server. All the necessary files are included in Redis Folder. More details can be found on the pages :
1. [Windows](https://github.com/microsoftarchive/redis/releases) 
2. [Linux](https://redis.io/download)

* To run the Application using Docker, please run the following commands:

        docker build .
        docker run -it <image_name/image_id>

image name/id is returned by bocker build. Custom redis installation is not required when you run the application using docker.

* To test the Create Campaign API, please use the following commands:

        curl --include \
        --header "Content-Type: application/json" \
        --request POST \
        --data '{"name":"Test", "keywords": ["technology"], "budget": 100.0}' \
        http://localhost:8080/campaigns

* To verify the Created Campaign, please use the following commands:
        
        curl --include http://localhost:8080/campaigns/<campaign_id>

        campaign_id: Campaign Id returned by the Create Campaign API.

* To test the Bidding API, please use the following commands:

        curl --include \
        --header "Content-Type: application/json" \
        --request POST \
        --data '{"bidId":"1", "keywords": ["technology"]}' \
        http://localhost:8080/bids

* Assumptions:

1. In case of exceptions, Bad Gateway Response would be sent.
