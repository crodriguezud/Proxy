FROM openjdk:latest
EXPOSE 8080
ADD /target/Proxy-0.0.1-SNAPSHOT.jar Proxy-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","Proxy-0.0.1-SNAPSHOT.jar"]