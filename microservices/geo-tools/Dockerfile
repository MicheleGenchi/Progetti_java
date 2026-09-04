# # Stage 1: Build stage (Maven + JDK 8)
FROM maven:3.9.6-eclipse-temurin-8 AS build
WORKDIR /geo-tools

# Copia il pom.xml per scaricare le dipendenze in un layer separato
COPY ./microservices/geo-tools/pom.xml pom.xml

# Sfrutta BuildKit per la cache delle dipendenze Maven durante il go-offline
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

# Copia i sorgenti
COPY ./microservices/geo-tools/src src

# Compila l'applicazione sfruttando la stessa cache
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests -B

# # Stage 2: Package stage (Runtime Java 8)
FROM eclipse-temurin:8-jre AS runtime

# Aggiornamento e installazione utility minime per healthcheck/troubleshooting
RUN apt-get update && apt-get upgrade -y && \
    apt-get install -y --no-install-recommends curl wget && \
    rm -rf /var/lib/apt/lists/*

ENV APP_HOME=/app
ARG JAVA_OPTS
ENV JAVA_TOOL_OPTIONS=${JAVA_OPTS}

# Configurazione utente non-root per la sicurezza
RUN groupadd -r appgroup && useradd -r -g appgroup -d $APP_HOME appuser

WORKDIR $APP_HOME

# Creazione cartelle necessarie e assegnazione permessi all'utente appuser
RUN mkdir -p config log && \
    chown -R appuser:appgroup $APP_HOME

VOLUME ["$APP_HOME/log", "$APP_HOME/config"]

# Copia del JAR finale (Sostituisci 'geo-tools.jar' con il nome reale se noto per evitare il wildcard)
COPY --from=build --chown=appuser:appgroup /geo-tools/target/*.jar app.jar

# Cambia l'utente corrente a non-root
USER appuser

EXPOSE 7000 5005

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]