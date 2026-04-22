import { useState } from "react";
import { graphqlRequest } from "../api/graphqlApi";
import { useAuth } from "../auth/AuthContext";

const DENTISTS_QUERY = `
  query {
    dentists {
      dentistId
      firstName
      lastName
      email
      specialization
    }
  }
`;

export default function DashboardPage() {
  const { token } = useAuth();
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function loadDentists() {
    setLoading(true);
    setError("");
    try {
      const data = await graphqlRequest(token, DENTISTS_QUERY);
      setResult(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="results">
      <h2>Dashboard</h2>
      <p className="muted">Use this button to verify authenticated GraphQL calls.</p>
      <button className="btn-primary" onClick={loadDentists} disabled={loading}>
        {loading ? "Loading..." : "Load Dentists"}
      </button>
      {error && <p className="error">{error}</p>}
      {result && <pre>{JSON.stringify(result, null, 2)}</pre>}
    </section>
  );
}

