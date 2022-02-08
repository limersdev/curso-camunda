FROM openjdk:11-jre-slim

RUN apt-get update && apt-get install curl -y

COPY ./target/*.jar /usr/local/lib/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
