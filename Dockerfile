FROM docker.io/library/eclipse-temurin:21-jdk-alpine@sha256:5b972454b90e7fe10044a8787daa22e42949a3dae762bc25f7a83705d04db6d3 AS builder

WORKDIR /src/be-achievement

COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean bootJar --no-daemon -x test

FROM docker.io/library/eclipse-temurin:21-jre-alpine@sha256:cfe82487280a59c5a0887b2ec08f50d389540950e373a8f3fdcd093c33428cf7 AS runner

ARG USER_NAME=adpro-a11
ARG USER_UID=1000
ARG USER_GID=${USER_UID}

RUN addgroup -g ${USER_GID} ${USER_NAME} \
    && adduser -h /opt/be-achievement -D -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}

USER ${USER_NAME}
WORKDIR /opt/be-achievement
COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/be-achievement/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]