FROM openjdk:latest

ARG JAR_FILE=target/Proxy-0.0.1-SNAPSHOT.jar

# cd /usr/local/runme
WORKDIR /usr/local/runme

# copy target/Proxy-0.0.1-SNAPSHOT.jar /usr/local/runme/app.jar
COPY ${JAR_FILE} app.jar

# copy project dependencies
# cp -rf target/lib/  /usr/local/runme/lib

# java -jar /usr/local/runme/app.jar
ENTRYPOINT ["java","-jar","app.jar"]