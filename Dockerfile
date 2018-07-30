## alpine linux with JRE
FROM openjdk:8-jre-alpine

## copy the spring jar
COPY target/*.jar /opt/myApp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/myApp.jar", "--spring.profiles.active=prod"]