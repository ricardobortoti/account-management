FROM gradle:jdk11 as builder

COPY --chown=gradle:gradle . /home/src
WORKDIR /home/src

RUN gradle build

FROM openjdk:11-jre-slim

# App
COPY --from=builder /home/src/build/libs/*.jar /app.jar

# Ports exposing
EXPOSE 9080

ENTRYPOINT ["java","-jar","/app.jar"]