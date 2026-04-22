## Plan: React Client for Spring GraphQL JWT

Create a separate React app and integrate it with existing backend auth and GraphQL contracts. The plan first aligns endpoint paths (current login is `/adsweb/api/v1/auth/login`, and register is not yet implemented), then adds a minimal auth flow (login/register), shared API client, JWT handling, and protected GraphQL calls. This reduces integration risk by defining API contracts before UI wiring.

### Steps
1. Confirm auth contract in `src/main/java/cs489/asd/lab/controller/AuthController.java` and `src/main/java/cs489/asd/lab/config/SecurityConfig.java`, then finalize login/register endpoint paths.
2. Add React app under `client/` with `src/pages`, `src/components`, `src/api`, `src/auth`, `src/routes`.
3. Create key files: `client/src/pages/LoginPage.tsx`, `RegisterPage.tsx`, `App.tsx`, and `AuthProvider`.
4. Implement API layer in `client/src/api/authApi.ts` and `graphqlApi.ts` with JWT `Authorization` injection.
5. Configure environment in `client/.env.example`: `VITE_API_BASE_URL`, `VITE_AUTH_LOGIN_PATH`, `VITE_AUTH_REGISTER_PATH`, `VITE_GRAPHQL_PATH`.
6. Verify end-to-end: register/login succeeds, token persists, protected `/graphql` queries return data, unauthorized states redirect to login.

### Further Considerations
1. Register support is missing now; add backend `POST /adsweb/api/v1/auth/register` or remove register UI until available?
2. Token persistence choice: localStorage (simple) or HttpOnly cookie (safer, needs backend cookie flow)?
3. Keep auth and GraphQL routing separate now, or add React Query/Apollo immediately for caching and retries?

