import { buildApiUrl } from "../api/http";

export default function ApiDocsPage() {
  const swaggerUrl = buildApiUrl("/swagger-ui/index.html");
  const apiDocsUrl = buildApiUrl("/v3/api-docs");

  return (
    <section className="card">
      <h2>API Documentation</h2>
      <p className="muted">
        Open the interactive Swagger UI or view the raw OpenAPI JSON for the backend.
      </p>

      <div className="form-actions">
        <a className="btn-primary" href={swaggerUrl} target="_blank" rel="noreferrer">
          Open Swagger UI
        </a>
        <a className="btn-secondary" href={apiDocsUrl} target="_blank" rel="noreferrer">
          OpenAPI JSON
        </a>
      </div>
    </section>
  );
}
