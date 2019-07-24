FROM openjdk:11 AS builder

WORKDIR /app
COPY / .
RUN df -m && free -m   #ls -altr; echo; cat .env

RUN sed 's/^/export /' .env > /app/export.env
RUN . /app/export.env && ./gradlew --no-daemon --console plain clean build

FROM openjdk:11
WORKDIR /app
COPY --from=builder /app /app

ENTRYPOINT ["/app/gradlew", "--no-daemon", "--console", "plain", "bootrun"]
