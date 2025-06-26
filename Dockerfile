# Multi-stage build per applicazioni Java
FROM maven:3.9-eclipse-temurin-17 AS build

# Crea directory di lavoro per il build
WORKDIR /app

# Copia pom.xml per ottimizzare il caching delle dipendenze
COPY pom.xml .

# Scarica le dipendenze (questa fase viene cachata se pom.xml non cambia)
RUN mvn dependency:go-offline -B

# Copia il codice sorgente
COPY src ./src

# Compila l'applicazione
RUN mvn clean package -DskipTests

# Stage finale - runtime
FROM eclipse-temurin:17-jre

# Installa PostgreSQL client per utility
RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*

# Crea utente non-root per sicurezza
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Crea directory di lavoro
WORKDIR /app

# Crea directory necessarie
RUN mkdir -p uploads logs && \
    chown -R appuser:appuser /app

# Copia il JAR compilato dal stage di build
COPY --from=build /app/target/*.jar app.jar

# Cambia proprietario del file
RUN chown appuser:appuser app.jar

# Espone la porta 8080 (porta standard Spring Boot)
EXPOSE 8080

# Cambia utente per sicurezza
USER appuser

# Comando per avviare l'applicazione Java
CMD ["java", "-jar", "app.jar"]