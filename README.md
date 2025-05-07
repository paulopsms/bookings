Projeto Bookings visto no curso de TDD pela FulLCycle desenvolvido em Java. Originalmente feito em NodeJS no curso.

Tecnologias utilizadas:

Dockerfile para build da aplicação:
- maven:3.8.5
- openjdk-17-slim

Docker-compose.yml
- Spring-Boot: 3.4.4
- Postgresql: 17.4-alpine
- Flyway: 11.8.0-alpine
- pgadmin4: latest

Para executar o projeto, basta executar o seguinte comando: 
docker compose up -d --build
