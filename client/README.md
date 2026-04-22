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

From the project root (`cs489-lab-7`):

```bash
docker compose up --build
```

Then open `http://localhost:5173`.

