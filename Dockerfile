# Usare una base image di Java
FROM openjdk:17-jdk-alpine

# Aggiungi il jar del progetto alla image
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Esporre la porta 8080
EXPOSE 8080

# Comando per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "/app.jar"]