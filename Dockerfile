FROM amazoncorretto:17.0.6

ARG JAR_FILE=build/libs/picky-be-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

RUN mkdir -p /src/main/resources/static/files/image \
             /src/main/resources/static/files/video \
             /src/main/resources/static/files/profile \
             /certs \
RUN echo "52.79.143.142 b79328b2be7b" >> /etc/hosts

COPY ./certs/truststore.jks /certs/truststore.jks

ENTRYPOINT ["java","-jar","/app.jar"]
