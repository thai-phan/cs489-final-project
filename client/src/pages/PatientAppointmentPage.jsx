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

      />
    </section>
  );
}
