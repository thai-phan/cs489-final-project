import {useEffect, useState} from "react";
import { restRequest } from "../api/restApi";
import { useAuth } from "../auth/AuthContext";
import DentistDirectory from "../components/DentistDirectory";
import DentistRegistrationForm from "../components/DentistRegistrationForm";

export default function DashboardPage() {
  const { token } = useAuth();
  const [registrations, setRegistrations] = useState([]);
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const dentists = [...registrations, ...(result ?? [])];

  useEffect(() => {
    loadDentists();
  }, []);

  async function loadDentists() {
    setLoading(true);
    setError("");
    try {
      const data = await restRequest(token, "GET", "/adsweb/api/v1/dentists");
      setResult(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="dashboard-layout">
      <DentistRegistrationForm
        dentists={dentists}
      />
      <DentistDirectory
        dentists={dentists}
        loading={loading}
        error={error}
        onLoadDentists={loadDentists}
      />
    </div>
  );
}
