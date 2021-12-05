FROM openjdk:11
COPY ./target/TalkABotTODOChallenge-1.0.0-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8182
ENTRYPOINT ["java", "-jar", "TalkABotTODOChallenge-1.0.0-SNAPSHOT.jar"]