FROM openjdk:17-jdk-alpine
ARG JAR_FILE=out/artifacts/techChallenge_jar/techChallenge.jar
COPY ${JAR_FILE} app.jar
ENV KAFKA_BROKER=kafka-broker
ENTRYPOINT ["java", "-Dkafka.bootstrap-servers=$KAFKA_BROKER:9092", "-jar","/app.jar"]
