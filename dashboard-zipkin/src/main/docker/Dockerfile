FROM openjdk:8-jre
MAINTAINER Jocelyn N'TAKPE <http://www.github.com/jntakpe>

ENV SPRING_PROFILES_ACTIVE docker
ENV DOCKERIZE_VERSION v0.3.0
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

VOLUME /tmp
ADD dashboard-zipkin.jar app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["dockerize", "-timeout", "60m", "-wait", "tcp://registry-server:8761", "-wait", "tcp://config-server:8888/info", \
 "java", "-jar", "/app.jar"]

EXPOSE 6262