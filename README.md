# Simple Spring AI QA application

This project contains a web service with the following endpoints under http://localhost:8080

* POST `/data/load`
* GET `/data/count`
* DELETE `/data/delete`
* GET `/question`

The `/question` endpoint takes a `question` parameter which is the question you want to ask the AI model.
The `/question` endpoint also takes a `prompstuff` boolean parameter, whose default it true, that will 'stuff the prompt' with
similar documents to the question.  When stuffing the prompt, this follows the RAG pattern.

## Prerequisites

### OpenAI Credentials

Create an account at [OpenAI Signup](https://platform.openai.com/signup) and generate the token at [API Keys](https://platform.openai.com/account/api-keys).

The Spring AI project defines a configuration property named `spring.ai.openai.api-key` that you should set to the value of the `API Key` obtained from `openai.com`.

You can set this in the projects `/resources/application.yml` file or by exporting an environment variable, for example.
```shell
export SPRING_AI_OPENAI_API_KEY=<INSERT KEY HERE>
```

The `/resources/application.yml` references the environment variable `${OPENAI_API_KEY}` as that is what the onboarding instructions for OpenAI suggest.

In short, Spring Boot provides many ways to set this property, pick a method that works for your needs.

## VectorStore

To run the PgVectorStore locally, using docker-compose.
From the top project directory and run:

```
docker-compose up
```

Later starts Postgres DB on localhost and port 5432.

Then you can connect to the database (password: `postgres`) and inspect or alter the `vector_store` table content:

```
psql -U postgres -h localhost -p 5432

\l
\c vector_db
\dt

select count(*) from vector_store;

delete from vector_store;
```

You can connect to the pgAdmin on http://localhost:5050  as user: `pgadmin4@pgadmin.org` and pass: `admin`.
Then navigate to the `Databases/vector_store/Schemas/public/Tables/vector_store`.

The UI tool [DBeaver](https://dbeaver.io/download/) is also a useful GUI for postgres.

## Building and running

```
./mvnw spring-boot:run
```

