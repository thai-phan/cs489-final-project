export const apiBaseUrl = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

function normalizeBaseUrl(baseUrl) {
  return typeof baseUrl === "string" ? baseUrl.trim().replace(/\/+$/, "") : "";
}

function normalizePath(path) {
  if (!path) {
    return "/";
  }

  return path.startsWith("/") ? path : `/${path}`;
}

export function buildApiUrl(path) {
  const baseUrl = normalizeBaseUrl(apiBaseUrl);
  const normalizedPath = normalizePath(path);

  return baseUrl ? `${baseUrl}${normalizedPath}` : normalizedPath;
}

export async function parseJson(response) {
  const payload = await response.json().catch(() => ({}));
  if (!response.ok) {
    const message = payload.message || payload.error || "Request failed";
    throw new Error(message);
  }
  return payload;
}

