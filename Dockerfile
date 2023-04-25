FROM gradle:jdk17-alpine AS BUILDER
WORKDIR /tmp/app
COPY settings.gradle .
COPY build.gradle .
COPY src src
RUN gradle build --no-daemon


FROM eclipse-temurin:17-jdk-alpine
WORKDIR /opt/app
COPY --from=BUILDER /tmp/app/build/libs/nbpapi-1.jar /opt/app/
EXPOSE 8080
ENTRYPOINT java -jar /opt/app/nbpapi-1.jar