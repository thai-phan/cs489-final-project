import { buildApiUrl, parseJson } from "./http";

export async function restRequest(token, method, path, body = null) {
  const response = await fetch(buildApiUrl(path), {
    method,
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: body ? JSON.stringify(body) : null
  });

  return parseJson(response);
}
