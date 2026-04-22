function formatDateTime(value) {
  if (!value) {
    return "N/A";
  }

  const parsed = new Date(value);
  if (Number.isNaN(parsed.getTime())) {
    return value;
  }

  return parsed.toLocaleString();
}

export default function PatientAppointmentsList({ appointments, loading, error }) {
  return (
    <>
      {error && <p className="error">{error}</p>}
      {!error && loading && <p className="muted">Loading appointment details...</p>}

      {!loading && !error && appointments.length > 0 ? (
        <table className="dentists-table">
          <thead>
            <tr>
              <th>Appointment ID</th>
              <th>Date & Time</th>
              <th>Status</th>
              <th>Dentist</th>
              <th>Specialization</th>
              <th>Email</th>
              <th>Surgery</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((appointment) => (
              <tr key={appointment.appointmentId}>
                <td>{appointment.appointmentId}</td>
                <td>{formatDateTime(appointment.appointmentDateTime)}</td>
                <td>{appointment.status}</td>
                <td>{appointment.dentistName}</td>
                <td>{appointment.dentistSpecialization}</td>
                <td>{appointment.dentistEmail}</td>
                <td>{appointment.surgeryLocation}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : null}

      {!loading && !error && appointments.length === 0 ? (
        <p className="muted">No appointments found.</p>
      ) : null}
    </>
  );
}
