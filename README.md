# CS489 Final Project

Monorepo for a full-stack ADS application:
- `server/`: Spring Boot + GraphQL + PostgreSQL (Gradle)
- `client/`: React + Vite frontend for auth and dashboard flows

## Project Structure

```text
cs489-final-project/
  client/         # React/Vite app
  server/         # Spring Boot backend
  docker-compose.yml
```

## Prerequisites

- Java 17+
- Node.js 18+
- Docker Desktop (optional, for containerized run)

## Option 1: Run with Docker Compose

From the repository root:

```bash
docker compose up --build
```

Expected service ports:
- Client: `http://localhost:5173`
- Backend: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

Stop containers:

```bash
docker compose down
```

Remove containers and database volume:

```bash
docker compose down -v
```

## Option 2: Run Locally (without Docker)

### 1) Start PostgreSQL
Create a local database and credentials that match server configuration (or override Spring datasource env vars).

### 2) Start backend

```bash
cd server
./gradlew bootRun
```

### 3) Start frontend

```bash
cd client
npm install
npm run dev
```

## Environment Notes

- Client env template: `client/.env.example`
- Common client env values:
  - `VITE_API_BASE_URL`
  - `VITE_AUTH_LOGIN_PATH`
  - `VITE_AUTH_REGISTER_PATH`
  - `VITE_GRAPHQL_PATH`

## Useful Links

- Client docs: `client/README.md`
- Server docs: `server/README.md`

## Troubleshooting

- If `npm run dev` fails, verify Node.js version and reinstall dependencies.
- If backend cannot connect to DB, verify datasource URL/user/password and PostgreSQL availability.
- If Docker build fails, check `docker-compose.yml` build paths and Dockerfiles for your local setup.

