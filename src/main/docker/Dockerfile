FROM openjdk:8-jre
MAINTAINER Charles-Eric GIRAUD <http://www.github.com/cegiraud>

ENV SPRING_PROFILES_ACTIVE docker
VOLUME /tmp
ADD build/libs/initiatives_backend-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java", "-jar", "/app.jar"]

EXPOSE 8080