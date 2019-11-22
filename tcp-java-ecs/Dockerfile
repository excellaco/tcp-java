FROM openjdk:11 AS builder

WORKDIR /app
# Fetch the gradle wrapper in a low layer:
COPY / .
RUN ./gradlew --no-daemon

RUN df -m && free -m   #ls -altr; echo; cat .env

# Prefix all non-comment lines in .env with "export ", write to export.env:
RUN sed 's/^\([^#]\)/export \1/' .env > /app/export.env
RUN . /app/export.env && ./gradlew --no-daemon --console plain bootJar && \
    mv /app/build/libs/tcp-java*jar /app/tcp-java.jar

FROM openjdk:11
WORKDIR /app
COPY --from=builder /app/tcp-java.jar /app

ENTRYPOINT ["java", "-jar", "/app/tcp-java.jar"]
