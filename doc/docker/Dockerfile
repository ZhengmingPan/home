FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD spring-boot-docker-1.0.jar home.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home.jar"]