FROM gradle:7.6.1-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew clean build --no-daemon

FROM openjdk:17-jdk-alpine
ARG MONGO_USERNAME
ARG MONGO_PASSWORD
ENV MONGODB_USERNAME=${MONGO_USERNAME}
ENV MONGODB_PASSWORD=${MONGO_PASSWORD}
COPY --from=build /home/gradle/src/build/libs/fintrack-1.0.0.jar /home/application/fintrack.jar
ENTRYPOINT ["java", "-jar", "/home/application/fintrack.jar"]