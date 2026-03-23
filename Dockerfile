# Etapa 1: Construcción (Build)
FROM eclipse-temurin:23-jdk AS builder
WORKDIR /app
COPY . .
# Damos permisos al wrapper de Maven
RUN chmod +x mvnw
# Compilamos el proyecto saltando los tests para ir más rápido
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:23-jre
WORKDIR /app
# Copiamos el JAR generado en la etapa anterior a esta nueva imagen limpia
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]