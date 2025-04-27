FROM openjdk:21-jdk

LABEL maintainer="siho-song"

ARG WORK_DIR=/app
ARG JAR_FILE_PATH=./build/libs/*.jar
ARG ENTRYPOINT_PATH=entrypoint.sh
ARG APP_NAME=8to10

WORKDIR ${WORK_DIR}

COPY ${JAR_FILE_PATH} ${WORK_DIR}/build/${APP_NAME}.jar
COPY ./entrypoint.sh /app/entrypoint.sh

RUN chmod +x ${WORK_DIR}/entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/bin/sh", "/app/entrypoint.sh"]