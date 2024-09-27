FROM openjdk:17-oraclelinux8

# install tools and create user as root
ARG USERNAME=vscode
ARG USER_GROUP_NAME=workspace
ARG USER_UID=1019
ARG USER_GID=1000
ARG PKG="git vim curl unzip zip sudo findutils wget"

SHELL ["/bin/bash", "-c"]

RUN microdnf update \
    && microdnf install ${PKG} \
    && groupadd --gid ${USER_GID} ${USER_GROUP_NAME} \
    && useradd --uid ${USER_UID} --shell /bin/bash --gid ${USER_GID} -m ${USERNAME} \
    && echo %${USERNAME} ALL=\(ALL\) NOPASSWD:ALL > /etc/sudoers.d/${USER_GROUP_NAME} \
    && chmod 0440 /etc/sudoers.d/${USER_GROUP_NAME}

RUN wget -q https://services.gradle.org/distributions/gradle-8.8-bin.zip \
    && unzip gradle-8.8-bin.zip -d /opt \
    && rm gradle-8.8-bin.zip

ENV GRADLE_HOME /opt/gradle-8.8
ENV PATH $PATH:/opt/gradle-8.8/bin

# prepare workspace as vscode
USER ${USERNAME}
RUN mkdir -p /home/${USERNAME}/.vscode-server/extensions \
    && chown -R ${USERNAME} \
    /home/${USERNAME}/.vscode-server

ARG APP_HOME=/apps
WORKDIR ${APP_HOME}/toy-log-parser

COPY ./toy-log-parser/build.gradle ${APP_HOME}/toy-log-parser/build.gradle
COPY ./toy-log-parser/settings.gradle ${APP_HOME}/toy-log-parser/settings.gradle
RUN gradle build --stacktrace

COPY . ${APP_HOME}

# Prepare entrypoints as root
USER root
COPY ./compose/production/spring/entrypoint /entrypoint
RUN sed -i 's/\r$//g' /entrypoint
RUN chmod +x /entrypoint

COPY ./compose/local/spring/start /start
RUN sed -i 's/\r$//g' /start
RUN chmod +x /start

ENTRYPOINT ["/entrypoint"]
