# ADS React Client

Minimal React client for ADS backend auth + GraphQL.

## Features
- Login (`POST /adsweb/api/v1/auth/login`)
- Register (`POST /adsweb/api/v1/auth/register`)
- JWT persistence in local storage
- Authenticated GraphQL call (`POST /graphql`)

## Setup
1. Copy `.env.example` to `.env` and update values if needed.
2. Start the Spring Boot backend.
3. Run the React app.

## Run
```bash
npm install
npm run dev
```

## Build
```bash
npm run build
```

## Run with Docker

From the project root (`cs489-final-project`):

```bash
docker compose up --build
```

Then open `http://localhost:5173`.

## Run on Minikube

The client image is meant to be built with an empty `VITE_API_BASE_URL` when
deploying through `k8s/minikube.yaml`, because the Nginx container proxies
API requests to the backend service inside the cluster.

Build command:

```bash
eval "$(minikube docker-env)"
docker build --build-arg VITE_API_BASE_URL= -t ads-client:minikube .
```

After applying the Kubernetes manifest, open the client URL with:

```bash
minikube service -n ads-minikube client --url
```

If you are running the frontend locally with `npm run dev`, the default
`VITE_API_BASE_URL=http://localhost:8080` from `.env.example` still works.

