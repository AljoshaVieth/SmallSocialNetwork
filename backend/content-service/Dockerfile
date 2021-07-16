FROM openjdk:11-jdk-slim
EXPOSE 8080:8080
WORKDIR /src
COPY . /src
RUN apt-get update
RUN apt-get install -y dos2unix
RUN dos2unix gradlew
RUN bash gradlew fatJar
WORKDIR /run
RUN cp /src/build/libs/*.jar /run/server.jar
CMD java -jar /run/server.jar