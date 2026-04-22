# syntax=docker/dockerfile:1

FROM node:20-alpine AS builder
WORKDIR /app
COPY package.json ./
RUN npm install
COPY . .
ARG VITE_API_BASE_URL=http://localhost:8080
ARG VITE_AUTH_LOGIN_PATH=/adsweb/api/v1/auth/login
ARG VITE_AUTH_REGISTER_PATH=/adsweb/api/v1/auth/register
ARG VITE_GRAPHQL_PATH=/graphql
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
ENV VITE_AUTH_LOGIN_PATH=$VITE_AUTH_LOGIN_PATH
ENV VITE_AUTH_REGISTER_PATH=$VITE_AUTH_REGISTER_PATH
ENV VITE_GRAPHQL_PATH=$VITE_GRAPHQL_PATH
RUN npm run build

FROM nginx:1.27-alpine
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

