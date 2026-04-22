import { useState } from "react";
import PatientAppointmentForm from "../components/PatientAppointmentForm";
import { restRequest } from "../api/restApi";

const INITIAL_FORM = {
  firstName: "",
  lastName: "",
  phone: "",
  email: "",
  preferredDentist: "",
  appointmentDate: "",
  appointmentTime: "",
  reason: "",
  contactMethod: "Phone Call"
};

export default function PatientAppointmentPage() {
  const [form, setForm] = useState(INITIAL_FORM);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  function updateField(field, value) {
    setForm(current => ({ ...current, [field]: value }));
  }

  async function onSubmit(event) {
    event.preventDefault();

    const trimmed = Object.fromEntries(
      Object.entries(form).map(([key, value]) => [key, typeof value === "string" ? value.trim() : value])
    );

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(trimmed.email)) {
      setError("Enter a valid email address.");
      setSuccess("");
      return;
    }

    if (!trimmed.appointmentDate || !trimmed.appointmentTime) {
      setError("Select a preferred appointment date and time.");
      setSuccess("");
      return;
    }

    setError("");
    setLoading(true);

    try {
      // Map preferred dentist to ID (simplified mapping)
      const dentistMapping = {
        "Dr. Emily Carter": 2,
        "Dr. Michael Tran": 3,
        "Dr. Sophia Patel": 4,
        "Dr. James Wilson": 5,
        "No Preference": 2 // Default to first dentist
      };

      const requestData = {
        patientId: 1, // Still using dummy for public form
        dentistId: dentistMapping[trimmed.preferredDentist] || 2,
        surgeryId: 1, // Default surgery
        appointmentDateTime: `${trimmed.appointmentDate}T${trimmed.appointmentTime}:00`
      };

      await restRequest(null, "POST", "/adsweb/api/v1/appointments/request", requestData);

      setSuccess(
        `Appointment request submitted for ${trimmed.firstName} ${trimmed.lastName}. A team member will follow up by ${trimmed.contactMethod.toLowerCase()}.`
      );
      setForm(INITIAL_FORM);
    } catch (err) {
      setError(err.message || "Failed to submit appointment request. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="public-hero">
      <div className="public-hero__intro">
        <p className="eyebrow">Patient Services</p>
        <h2>Request a Dental Appointment</h2>
        <p className="muted">
          Patients can submit an appointment request online without signing in. The office team
          will review the request and confirm the appointment details.
        </p>
      </div>

      <PatientAppointmentForm
        form={form}
        onFieldChange={updateField}
        onSubmit={onSubmit}
        onClear={() => {
          setForm(INITIAL_FORM);
          setError("");
          setSuccess("");
        }}
        error={error}
        success={success}
        loading={loading}
      />
    </section>
  );
}
