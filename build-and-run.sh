#!/bin/bash

echo "Parando e removendo containers antigos..."
docker-compose down

echo "Removendo imagem antiga..."
docker rmi rodkrtz/paysync:latest || true

echo "Gerando o pacote JAR..."
./mvnw clean package -DskipTests

echo "Criando a imagem Docker..."
docker build -t rodkrtz/paysync:latest .

echo "Iniciando aplicação com Docker Compose..."
docker-compose up -d --build

echo "Aplicação rodando na porta 8080!"