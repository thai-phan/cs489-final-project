import ManagerAppointmentsList from "../components/ManagerAppointmentsList.jsx";

export default function ListAppointmentManager() {
  return (
      <section className="results">
        <div className="section-heading">
          <h2>{"All Appointments"}</h2>
          <p className="muted">
            View all pending appointments in the system.
          </p>
        </div>
        <ManagerAppointmentsList/>
      </section>
  );
}
