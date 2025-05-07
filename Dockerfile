# Build stage - compilar o projeto
FROM maven:3.8.5-openjdk-17-slim AS build

# Diretório de trabalho para o Maven
WORKDIR /app

# Copiar o arquivo pom.xml e baixar as dependências para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline --no-transfer-progress

# Copiar o restante dos arquivos do projeto
COPY src ./src

# Compilar o projeto e criar o arquivo JAR (sem executar os testes para acelerar o build)
RUN mvn clean package -DskipTests --no-transfer-progress


# Runtime stage - executar a aplicação
# Usar uma imagem JRE leve e otimizada para produção
FROM openjdk:17.0.1-jdk-slim

# Criar um diretório de trabalho para a aplicação
WORKDIR /app

# Copiar o arquivo JAR gerado na etapa de build
COPY --from=build /app/target/*.jar application.jar

# Criar um usuário não root para rodar a aplicação de forma segura
RUN useradd --create-home --shell /bin/bash spring
RUN chown -R spring:spring /app
USER spring

# Variável de ambiente para opções da JVM (configurações padrão ajustáveis)
ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV SPRING_PROFILES_ACTIVE="default"

# Expor a porta do aplicativo (porta padrão do Spring Boot)
EXPOSE 8080

# Comando de execução da aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar application.jar"]