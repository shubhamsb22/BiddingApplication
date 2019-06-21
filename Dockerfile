FROM openjdk:8-jdk-alpine

MAINTAINER  Shubham Baviskar <shubhamsbaviskar@gmail.com>

RUN apk --update add maven
RUN apk --update add redis && rm  -rf /tmp/* /var/cache/apk/*

COPY target/biddingapplication-0.0.1-SNAPSHOT.jar biddingapplication.jar
COPY config.yml config.yml

EXPOSE 8080

CMD [ "sh", "-c", "redis-server --daemonize yes; java -jar biddingapplication.jar server config.yml" ]