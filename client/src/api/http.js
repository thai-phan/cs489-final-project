const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

export function buildApiUrl(path) {
  return `${apiBaseUrl}${path}`;
}

export async function parseJson(response) {
  const payload = await response.json().catch(() => ({}));
  if (!response.ok) {
    const message = payload.message || payload.error || "Request failed";
    throw new Error(message);
  }
  return payload;
}

