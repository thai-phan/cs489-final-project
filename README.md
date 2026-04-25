# CS489 Final Project

Monorepo for a full-stack ADS application:
- `server/`: Spring Boot + GraphQL + PostgreSQL (Gradle)
- `client/`: React + Vite frontend for auth and dashboard flows
- `k8s/minikube.yaml`: Kubernetes manifests for a local Minikube deployment

## Project Structure

```text
cs489-final-project/
  client/         # React/Vite app
  server/         # Spring Boot backend
  k8s/            # Kubernetes manifests for Minikube
  docker-compose.yml
```

## Prerequisites

- Java 17+
- Node.js 18+
- Docker Desktop (optional, for containerized run)
- Minikube + `kubectl` (for Kubernetes deployment)

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

## Option 2: Run on Minikube

This project ships with a single Kubernetes manifest at `k8s/minikube.yaml`.
It runs PostgreSQL, the Spring Boot backend, and the React client in one namespace.

### 1) Start Minikube

```bash
minikube start
```

### 2) Build the images inside Minikube's Docker environment

```bash
eval "$(minikube docker-env)"
docker build -t ads-backend:minikube ./server
docker build --build-arg VITE_API_BASE_URL= -t ads-client:minikube ./client
```

The client is built with an empty API base URL because its Nginx config proxies
`/adsweb/api/v1/*` and `/graphql` to the backend service inside the cluster.

### 3) Apply the manifests

```bash
kubectl apply -f k8s/minikube.yaml
```

### 4) Open the app

Wait for the pods to become ready, then get the client URL:

```bash
minikube service -n ads-minikube client --url
```

The backend is available inside the cluster as `http://backend:8080`.
If you want to debug it directly, use port-forwarding:

```bash
kubectl -n ads-minikube port-forward svc/backend 8080:8080
```

### 5) Clean up

```bash
kubectl delete -f k8s/minikube.yaml
minikube delete
```

## Option 3: Run Locally (without Docker)

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
- Minikube manifest: `k8s/minikube.yaml`

## Troubleshooting

- If `npm run dev` fails, verify Node.js version and reinstall dependencies.
- If backend cannot connect to DB, verify datasource URL/user/password and PostgreSQL availability.
- If Docker build fails, check `docker-compose.yml` build paths and Dockerfiles for your local setup.

