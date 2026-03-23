# Usamos una imagen de Java 21 (como la que tienes instalada)
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
# Copiamos el .jar que genera Maven (asegúrate de que el nombre coincida)
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]