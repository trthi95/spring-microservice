FROM openjdk:11.0.11-jre
VOLUME /tmp
ENV DATABASE_HOST=host.docker.internal
ENV DATABASE_PORT=5432
ENV DATABASE_NAME=learn-security
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=postgres
ADD target/learn-spring-security-0.0.1-SNAPSHOT.jar app.jar
 
ENTRYPOINT exec java -jar app.jar