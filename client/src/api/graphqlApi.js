import { buildApiUrl, parseJson } from "./http";

const graphQlPath = import.meta.env.VITE_GRAPHQL_PATH || "/graphql";

export async function graphqlRequest(token, query, variables = {}) {
  const response = await fetch(buildApiUrl(graphQlPath), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify({ query, variables })
  });

  const payload = await parseJson(response);
  if (payload.errors?.length) {
    throw new Error(payload.errors[0].message || "GraphQL request failed");
  }
  return payload.data;
}

