import { useState } from "react";
import { restRequest } from "../api/restApi";
import { useAuth } from "../auth/AuthContext";
import DentistDirectory from "../components/DentistDirectory";
import DentistRegistrationForm from "../components/DentistRegistrationForm";

const INITIAL_FORM = {
  dentistId: "",
  firstName: "",
  lastName: "",
  phone: "",
  email: "",
  specialization: ""
};

export default function DashboardPage() {
  const { token } = useAuth();
  const [form, setForm] = useState(INITIAL_FORM);
  const [registrations, setRegistrations] = useState([]);
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [formError, setFormError] = useState("");
  const [formSuccess, setFormSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const dentists = [...registrations, ...(result ?? [])];

  function updateField(field, value) {
    setForm(current => ({ ...current, [field]: value }));
  }

  function onSubmit(event) {
    event.preventDefault();
    const trimmed = Object.fromEntries(
      Object.entries(form).map(([key, value]) => [key, value.trim()])
    );

    if (!/^\d+$/.test(trimmed.dentistId)) {
      setFormError("Dentist ID must be a numeric value.");
      setFormSuccess("");
      return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(trimmed.email)) {
      setFormError("Enter a valid email address.");
      setFormSuccess("");
      return;
    }

    if (dentists.some(dentist => String(dentist.dentistId) === trimmed.dentistId)) {
      setFormError("Dentist ID already exists in the current list.");
      setFormSuccess("");
      return;
    }

    const newDentist = {
      dentistId: Number(trimmed.dentistId),
      firstName: trimmed.firstName,
      lastName: trimmed.lastName,
      phone: trimmed.phone,
      email: trimmed.email,
      specialization: trimmed.specialization
    };

    setRegistrations(current => [newDentist, ...current]);
    setForm(INITIAL_FORM);
    setFormError("");
    setFormSuccess("Dentist registration has been captured in the UI.");
  }

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
        form={form}
        onFieldChange={updateField}
        onSubmit={onSubmit}
        onClear={() => {
          setForm(INITIAL_FORM);
          setFormError("");
          setFormSuccess("");
        }}
        formError={formError}
        formSuccess={formSuccess}
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
