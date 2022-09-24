FROM openjdk:11
LABEL maintainer="vidya.sagar"
EXPOSE 8081
ADD target/tweet_app-0.0.1-SNAPSHOT.jar tweet_app_backend.jar
ENTRYPOINT ["java","-jar","/tweet_app_backend.jar"]