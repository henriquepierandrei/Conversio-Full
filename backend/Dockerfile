# Etapa de build
FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copia apenas o arquivo pom.xml primeiro para aproveitar cache de dependências
COPY pom.xml .

# Baixa dependências antes de copiar o código-fonte
RUN mvn dependency:go-offline

# Agora copia o código-fonte
COPY src ./src

# Compila o projeto e gera o JAR
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:23-jdk
WORKDIR /app

# Copia o JAR gerado na etapa de build
COPY --from=build /app/target/Conversio-0.0.1.jar app.jar

# Expõe a porta 8080 para o container
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]