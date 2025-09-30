# Sistema de Controle de Doações

Este projeto é uma aplicação web desenvolvida como trabalho final das disciplinas de **Engenharia de Software III** e **Programação Back-End** na UCPel.  
O sistema tem como objetivo apoiar o **Asilo de Mendigos de Pelotas**, permitindo o **registro, organização e acompanhamento de doações** realizadas pela comunidade.

## 📋 Funcionalidades

- Cadastro, listagem, atualização e exclusão de doadores.
- Registro de doações financeiras e de produtos.
- Listagem de doações por doador.

## 🗂️ Arquitetura

A aplicação segue uma arquitetura de **três camadas**:

- **Front-end**: Angular
- **Back-end**: Java 17 + Spring Boot
- **Banco de dados**: PostgreSQL  
  Além disso, a solução é totalmente **containerizada com Docker**, facilitando a implantação e a portabilidade.

## 🚀 Executando com Docker

### Pré-requisitos:

- Docker e Docker Compose instalados na máquina.

### Instruções:

1. Clone o repositório:

   ```bash
   git clone https://github.com/LuisEdDias/projeto-vi-a
   cd projeto-vi-a
   ```

2. Construa e suba os containers:

   ```bash
   docker-compose up -d --build
   ```

3. Clone o repositório:
- Backend (API): http://localhost:8080
- Frontend (Angular): http://localhost:80

## 🛠️ Tecnologias

### Backend
- Java 17
- Spring Boot 3.5
- PostgreSQL 16

### Frontend
- Angular 19
- Bootstrap 5

### Infraestrutura
- Docker

## 📚 Autor

Luís Eduardo Dias — UCPel 2025


[![LinkedIn](https://img.shields.io/badge/LinkedIn-Perfil-blue?logo=linkedin)](https://www.linkedin.com/in/luisvdias94)
