# Stage 1: Build the application
FROM eclipse-temurin:23-jdk AS builder
WORKDIR /app

# 1. Copiar solo los archivos de configuración de Maven primero
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# 2. Descargar dependencias (esto se cacheará si el pom.xml no cambia)
# Usamos go-offline para bajar todo antes de copiar el código fuente
RUN ./mvnw dependency:go-offline -B

# 3. Ahora copiar el código fuente y compilar
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Run the application
FROM eclipse-temurin:23-jre
WORKDIR /app
# Es mejor ser específico con el nombre del JAR si lo conoces
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]