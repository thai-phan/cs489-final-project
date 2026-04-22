import { useEffect, useState } from "react";
import PatientAppointmentsList from "../components/PatientAppointmentsList";
import { restRequest } from "../api/restApi";
import { useAuth } from "../auth/AuthContext";

export default function MyAppointmentsPage() {
  const { token, roles } = useAuth();
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const isManager = roles.includes("ROLE_MANAGER");

  useEffect(() => {
    let cancelled = false;

    async function loadAppointments() {
      setLoading(true);
      setError("");

      try {
        const path = isManager ? "/adsweb/api/v1/appointments/pending" : "/adsweb/api/v1/appointments/my";
        const data = await restRequest(token, "GET", path);
        if (cancelled) {
          return;
        }

        const mappedAppointments = (data ?? []).map((appointment) => ({
          ...appointment,
          dentistName: appointment.dentist
            ? `${appointment.dentist.firstName} ${appointment.dentist.lastName}`
            : `Dentist #${appointment.dentistId}`,
          dentistSpecialization: appointment.dentist?.specialization || "N/A",
          dentistEmail: appointment.dentist?.email || "N/A"
        }));

        setAppointments(mappedAppointments);
      } catch (err) {
        if (!cancelled) {
          setError(err.message);
          setAppointments([]);
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    loadAppointments();

    return () => {
      cancelled = true;
    };
  }, [token, isManager]);

  return (
    <section className="results">
      <div className="section-heading">
        <h2>{isManager ? "All Appointments" : "My Appointments"}</h2>
        <p className="muted">
          {isManager
            ? "View all pending appointments in the system."
            : "View your booked appointments and the dentist assigned to each visit."
          }
        </p>
      </div>

      <PatientAppointmentsList appointments={appointments} loading={loading} error={error} />
    </section>
  );
}
