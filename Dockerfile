# Fornece o runtime Java 21 para executar o JAR.
FROM eclipse-temurin:21-jre

# Define o diretório de trabalho para as instruções seguintes.
WORKDIR /app

# Copia somente o JAR produzido por mvn clean package.
COPY target/ludoteca-api.jar app.jar

# Documenta a porta interna. O Compose publica a porta no host.
EXPOSE 8080

# Executa a aplicação quando o container inicia.
ENTRYPOINT ["java", "-jar", "app.jar"]
