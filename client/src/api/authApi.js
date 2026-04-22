import { buildApiUrl, parseJson } from "./http";

const loginPath = import.meta.env.VITE_AUTH_LOGIN_PATH || "/adsweb/api/v1/auth/login";
const registerPath = import.meta.env.VITE_AUTH_REGISTER_PATH || "/adsweb/api/v1/auth/register";

export async function loginRequest(username, password) {
  const response = await fetch(buildApiUrl(loginPath), {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password })
  });
  return parseJson(response);
}

export async function registerRequest(username, password) {
  const response = await fetch(buildApiUrl(registerPath), {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password })
  });
  return parseJson(response);
}

