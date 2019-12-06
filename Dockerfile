FROM openjdk:11 AS builder

# Add a system user and group api:api
RUN useradd -r -d /home/api -u 600 -U   api
RUN mkdir /app /home/api && chown api:api /app /home/api

USER api
WORKDIR /app
COPY --chown=api:api / .

RUN df -m && free -m

# Prefix all non-comment lines in .env with "export ", write to export.env:
RUN sed 's/^\([^#]\)/export \1/' .env > /app/export.env
RUN . /app/export.env && ./gradlew --no-daemon --console plain clean build

FROM openjdk:11

RUN useradd -r -d /home/api -u 600 -U   api
RUN mkdir /app /home/api && chown api:api /app /home/api

USER api
WORKDIR /app
COPY --from=builder --chown=api:api /app /app

ENTRYPOINT ["/app/gradlew", "--no-daemon", "--stacktrace", "--console", "plain", "bootrun"]
