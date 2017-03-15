FROM openjdk:8-jre
MAINTAINER Jocelyn N'TAKPE <http://www.github.com/jntakpe>

ENV SPRING_PROFILES_ACTIVE docker

VOLUME /tmp
ADD registry-server.jar app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java", "-jar", "/app.jar"]

EXPOSE 8761