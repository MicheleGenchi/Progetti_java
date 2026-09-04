@echo off

set rootPath=%userprofile%\Documents\progetti_java

echo Enter your nome progetto : 
set /p NOMEPROGETTO=

echo Enter port progetto : 
set /p PORTA=

if not exist microservices\%NOMEPROGETTO% goto next

SET /P CHOICE=Folder exist, remove ([Y]/N)?
IF /i "%CHOICE%" NEQ "N" GOTO next
rmdir /S /Q microservices\%NOMEPROGETTO%
echo Verifica se %NOMEPROGETTO% è stata rimossa e premi un tasto per continuare
pause >nul
:next

md microservices\%NOMEPROGETTO%
cd microservices\%NOMEPROGETTO%

curl -sS https://start.spring.io/starter.tgz ^
 -d name=%NOMEPROGETTO%^
 -d type=maven-project^
 -d bootVersion=2.7.12^
 -d dependencies=web,devtools^
 -d groupId=comgenchi^
 -d artifactId=%NOMEPROGETTO%^
 -d javaVersion=1.8 | tar -xzvf -

(
echo #
echo # Build stage use debian
echo #
echo FROM maven:3.6.3-jdk-8-slim AS build
echo RUN mkdir -p /%NOMEPROGETTO%
echo WORKDIR /%NOMEPROGETTO%
echo CMD cd /%NOMEPROGETTO%
echo COPY ./microservices/%NOMEPROGETTO%/src src
echo COPY ./microservices/%NOMEPROGETTO%/pom.xml pom.xml
echo RUN mvn dependency:go-offline -B
echo RUN --mount=type=cache,target=/root/.m2 mvn package
)>Dockerfile

(
echo #
echo # Package stage use debian
echo #
echo FROM openjdk:8-jre-slim as runtime
echo RUN apt-get update && apt-get install -y zip git zsh wget curl
echo RUN sh -c "$(wget -O- https://github.com/deluan/zsh-in-docker/releases/download/v1.1.5/zsh-in-docker.sh)"
echo ENV APP_HOME /app
echo # Possibility to set JVM options ^(https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html^)
echo ARG JAVA_OPTS
echo ENV JAVA_OPTS=$JAVA_OPTS
echo ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n
echo # Create base app folder
echo RUN mkdir $APP_HOME
echo # Create folder to save configuration files
echo RUN mkdir $APP_HOME/config
echo # Create folder with application logs
echo RUN mkdir $APP_HOME/log
echo VOLUME $APP_HOME/log
echo VOLUME $APP_HOME/config
echo WORKDIR $APP_HOME
echo # Copy executable jar file from the builder image
echo COPY --from=build /%NOMEPROGETTO%/target/*.jar app.jar
echo COPY --from=build /root/.m2 /root/.m2
echo EXPOSE %PORTA%
echo EXPOSE 5005
echo ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
)>>Dockerfile

cd %rootPath%
rem DOCKER-COMPOSE CONTAINER
(
echo.
echo # microservice %NOMEPROGETTO%
echo   %NOMEPROGETTO%:
echo     container_name: %NOMEPROGETTO%
echo     build:
echo       context: .
echo       dockerfile: microservices/%NOMEPROGETTO%/Dockerfile
echo     env_file: ./microservices/db/.env.db-geo
echo     environment:
echo       - JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
echo     volumes:
echo       - ./microservices/%NOMEPROGETTO%/src:/geo-tools/src # cartella progetto
echo     stdin_open: true
echo     tty: true
echo     extra_hosts:
echo       - "host.docker.internal:host-gateway"
echo     ports:
echo       - %PORTA%:8080
echo       - 5005:5005
)>>docker-compose.yml

rem Build the image
docker-compose build
rem Run the image
docker-compose up -d

goto fine

:progetto_esiste
echo il progetto è già presente

:fine
cd %rootPath%
echo buon lavoro!