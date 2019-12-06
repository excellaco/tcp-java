FROM openjdk:11 AS builder

RUN groupadd -g 600 api && useradd -r -u 600 -g api api
RUN mkdir /home/api && chown api:api /home/api

WORKDIR /app
COPY / .
RUN chown -R api:api /app

USER api
RUN df -m && free -m

# Prefix all non-comment lines in .env with "export ", write to export.env:
RUN sed 's/^\([^#]\)/export \1/' .env > /app/export.env
RUN . /app/export.env && ./gradlew --no-daemon --console plain clean build

FROM openjdk:11

RUN groupadd -g 600 api && useradd -r -u 600 -g api api
RUN mkdir /home/api && chown api:api /home/api

WORKDIR /app
COPY --from=builder /app /app
RUN chown -R api:api /app
USER api

ENTRYPOINT ["/app/gradlew", "--no-daemon", "--stacktrace", "--console", "plain", "bootrun"]
