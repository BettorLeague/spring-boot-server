## alpine linux with JRE
FROM openjdk:8-jre-alpine

## copy the spring jar
COPY target/*.jar /opt/myApp.jar

CMD ["/usr/bin/java", "-jar", "/opt/myApp.jar", "--spring.profiles.active=prod"]
EXPOSE 8080