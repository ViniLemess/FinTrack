FROM gradle:8.8-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew clean build --no-daemon

FROM openjdk:21
ARG MONGO_USERNAME
ARG MONGO_PASSWORD
ENV MONGODB_USERNAME=${MONGO_USERNAME}
ENV MONGODB_PASSWORD=${MONGO_PASSWORD}
COPY --from=build /home/gradle/src/build/libs/br.com.vinilemess.fintrack-all.jar /home/application/fintrack.jar
ENTRYPOINT ["java", "-jar", "/home/application/fintrack.jar"]