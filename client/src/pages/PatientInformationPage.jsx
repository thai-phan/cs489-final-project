import { useState } from "react";
import PatientInformationForm from "../components/PatientInformationForm";

const INITIAL_FORM = {
  firstName: "",
  lastName: "",
  phone: "",
  email: "",
  mailingAddress: "",
  dateOfBirth: ""
};

export default function PatientInformationPage() {
  const [form, setForm] = useState(INITIAL_FORM);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  function onSubmit(event) {
    event.preventDefault();

    const trimmed = Object.fromEntries(
      Object.entries(form).map(([key, value]) => [key, typeof value === "string" ? value.trim() : value])
    );

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(trimmed.email)) {
      setError("Enter a valid email address.");
      setSuccess("");
      return;
    }

    if (!trimmed.dateOfBirth) {
      setError("Select the patient date of birth.");
      setSuccess("");
      return;
    }

    setError("");
    setSuccess(
      `Patient information captured for ${trimmed.firstName} ${trimmed.lastName}. The office team can now continue enrollment.`
    );
    setForm(INITIAL_FORM);
  }

  return (
    <section className="public-hero">
      <div className="public-hero__intro">
        <p className="eyebrow">Patient Intake</p>
        <h2>Patient Information</h2>
        <p className="muted">
          Use this page to collect a new patient&apos;s contact details, mailing address, and date
          of birth before scheduling services.
        </p>
      </div>

      <PatientInformationForm
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
      />
    </section>
  );
}
