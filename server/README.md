# ADS Backend (Spring Boot + GraphQL + PostgreSQL)

Backend service for the CS489 final project.

## Tech Stack

- Spring Boot (Web, Security, GraphQL, Data JPA)
- PostgreSQL
- Gradle (Java 17 toolchain)
- JWT-based authentication

## Key Paths

- `src/main/java/cs489/asd/lab/Application.java`
- `src/main/java/cs489/asd/lab/controller/HealthController.java`
- `src/main/java/cs489/asd/lab/controller/AuthController.java`
- `src/main/java/cs489/asd/lab/controller/graphql/`
- `src/main/resources/application.properties`
- `src/main/resources/graphql/schema.graphqls`
- `src/main/resources/schema.sql`
- `src/main/resources/data.sql`

## Run Locally

From `server/`:

```bash
./gradlew bootRun
```

The app uses profile-based config:
- `local` (default): local PostgreSQL + GraphiQL enabled
- `aws`: cloud-friendly defaults + GraphiQL disabled

The `local` profile uses PostgreSQL at `jdbc:postgresql://localhost:5432/cs489-project` by default.

To run with the AWS profile locally for validation:

```bash
SPRING_PROFILES_ACTIVE=aws \
SPRING_DATASOURCE_URL='jdbc:postgresql://<host>:5432/<db>' \
SPRING_DATASOURCE_USERNAME='<username>' \
SPRING_DATASOURCE_PASSWORD='<password>' \
APP_JWT_SECRET='<long-random-secret>' \
./gradlew bootRun
```

## Basic Endpoints

- `GET /` service status
- `GET /db/ping` database connectivity check
- `POST /adsweb/api/v1/auth/register` create user and return JWT
- `POST /adsweb/api/v1/auth/login` authenticate and return JWT
- `POST /graphql` GraphQL endpoint (requires Bearer token)
- `GET /graphiql` GraphiQL UI

## Authentication Examples

Register:

```bash
curl -X POST http://localhost:8080/adsweb/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"secret123"}'
```

Login:

```bash
curl -X POST http://localhost:8080/adsweb/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"secret123"}'
```

Use `accessToken` from the response as `Authorization: Bearer <token>`.

## GraphQL Example

```graphql
query {
  dentists {
	dentistId
	firstName
	lastName
	email
  }
}
```

Example curl (replace `<TOKEN>`):

```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"query":"query { dentists { dentistId firstName lastName email } }"}'
```

## Docker (From Repository Root)

```bash
docker compose up --build
```

Stop:

```bash
docker compose down
```

Remove containers and DB volume:

```bash
docker compose down -v
```

## Minikube / Kubernetes

The repository includes `k8s/minikube.yaml`, which deploys PostgreSQL,
the backend, and the React client into a local Minikube namespace.

The backend pod is configured with:
- `SPRING_PROFILES_ACTIVE=local`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/cs489-project`
- `SPRING_SQL_INIT_MODE=always`

That startup mode is important because `src/main/resources/schema.sql` and
`src/main/resources/data.sql` bootstrap the schema and seed data.

Build and load the backend image before applying the manifest:

```bash
eval "$(minikube docker-env)"
docker build -t ads-backend:minikube .
kubectl apply -f ../k8s/minikube.yaml
```

Useful debugging commands:

```bash
kubectl -n ads-minikube get pods
kubectl -n ads-minikube logs deploy/backend
kubectl -n ads-minikube port-forward svc/backend 8080:8080
```

## AWS Cloud Configuration

The backend now includes `application-aws.properties` for cloud deployments.

Set these environment variables in your AWS runtime (ECS/Elastic Beanstalk/EC2):
- `SPRING_PROFILES_ACTIVE=aws`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `APP_JWT_SECRET`

Optional overrides:
- `SPRING_JPA_DEFAULT_SCHEMA` (default: `schema1`)
- `SPRING_JPA_HIBERNATE_DDL_AUTO` (default: `validate`)
- `SPRING_SQL_INIT_MODE` (default: `never`)
- `APP_JWT_EXPIRATION_SECONDS` (default: `3600`)
- `PORT` (default: `8080`)

