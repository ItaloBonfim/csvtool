# 📊 CSVTool

**CSVTool** é uma aplicação web desenvolvida em Java com Spring Boot que permite importar arquivos CSV, visualizar, editar e exportar os dados de forma eficiente, utilizando persistência em banco de dados PostgreSQL com suporte a Docker, paginação e processamento assíncrono.

---

## 🚀 Funcionalidades

- ✅ Tela de login com autenticação via Spring Security
- ✅ Upload de arquivos CSV com validação de dados
- ✅ Visualização de dados com filtros por nome, sobrenome e email
- ✅ Edição de registros antes da persistência
- ✅ Persistência no banco de dados após confirmação
- ✅ Histórico de uploads com status (`PENDING`, `COMPLETED`)
- ✅ Exportação de dados com geração de CSV em lote e barra de progresso
- ✅ Redirecionamento automático ao finalizar o download
- ✅ Estrutura baseada em DDD e boas práticas de arquitetura

---

## 🧱 Tecnologias Utilizadas

- Java 21
- Spring Boot 3+
- Spring Security
- Spring Data JPA
- PostgreSQL (via Docker)
- Flyway (controle de versão do banco)
- Thymeleaf + Bootstrap 5
- Async Batch Export
- Docker Compose (ambiente de banco)

---

## 🔓 Login
- usuario : admin
- senha : admin
---
# ⚠️ Cuidado com o padrão do csv utilizado
CSV's podem possuir muito formatos diferentes, e colocar todos eles dentro de uma unica ferramente é um baita desafio.
Para esse projeto em especifico, a base do csv consiste no seguinte padrão:
### Header
```Nome,SobreNome,Email,Sexo,IpAcesso,Idade,Nascimento```
### Linhas/dados
```Devinne,Gorthy,dgorthy0@over-blog.com,Female,211.43.226.128,26,16/2/16419692``` \
Todos esses dados devem estar concatenados na coluna A e na quantidade de linhas que você conseguir carregar dentro de um csv
o sistema ira carregar esses dados de forma assíncrona e em lotes. Isso permite melhor processamento e eficiência ao processar os dados


## ⚙️ Como Executar Localmente

### Pré-requisitos

- Java 21
- Docker
- Git
- IDE como IntelliJ ou VSCode

### 1. Clone o projeto

```bash```
git clone https://github.com/ItaloBonfim/csvtool.git
cd csvtool

### 2 Suba o banco de dados
```docker-compose up -d```

### 3 Execute o projeto
Abra o projeto em sua IDE, aguarde o Flyway criar as tabelas automaticamente e rode a aplicação CsvToolApplication.

### Docker hub
docker run proitalosouza/csvtool:lateast
