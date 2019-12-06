FROM openjdk:11 AS builder

RUN groupadd -g 600 api && useradd -r -u 600 -g api api
RUN mkdir /home/api && chown api:api /home/api

WORKDIR /app

# Fetch the gradle wrapper in a low layer:
COPY / .
RUN chown -R api:api /app

USER api
RUN ls -altr /app/.gradle ; du -sk /app/.gradle
RUN cd /app/gradlehome/wrapper/dists/gradle-5.2.1-all/* && unzip -q gradle-5.2.1-all.zip
#RUN echo "HOME is $HOME"; export GRADLE_USER_HOME=/app/gradlehome ; ./gradlew --no-daemon

RUN df -m && free -m

# Prefix all non-comment lines in .env with "export ", write to export.env:
RUN sed 's/^\([^#]\)/export \1/' .env > /app/export.env
RUN . /app/export.env && export GRADLE_USER_HOME=/app/gradlehome && ./gradlew --no-daemon --console plain bootJar && \
    mv /app/build/libs/tcp-java*jar /app/tcp-java.jar

FROM openjdk:11

RUN groupadd -g 600 api && useradd -r -u 600 -g api api
RUN mkdir /home/api && chown api:api /home/api

WORKDIR /app
COPY --from=builder /app/tcp-java.jar /app
RUN chown -R api:api /app
USER api

ENTRYPOINT ["java", "-jar", "/app/tcp-java.jar"]
