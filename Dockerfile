FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/employeesmanagement-0.0.1-SNAPSHOT.jar /app/employeesmanagement.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "employeesmanagement.jar"]
