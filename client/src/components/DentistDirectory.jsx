export default function DentistDirectory({ dentists, loading, error, onLoadDentists }) {
  return (
    <section className="results">
      <div className="section-heading section-heading-row">
        <div>
          <h2>Dentist Directory</h2>
          <p className="muted">
            Review registered dentists and load the current directory from the API.
          </p>
        </div>
        <button className="btn-primary" onClick={onLoadDentists} disabled={loading}>
          {loading ? "Loading..." : "Load Existing Dentists"}
        </button>
      </div>

      {error && <p className="error">{error}</p>}

      {dentists.length > 0 ? (
        <table className="dentists-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Phone</th>
              <th>Email</th>
              <th>Specialization</th>
            </tr>
          </thead>
          <tbody>
            {dentists.map((dentist) => (
              <tr key={dentist.dentistId}>
                <td>{dentist.dentistId}</td>
                <td>{dentist.firstName}</td>
                <td>{dentist.lastName}</td>
                <td>{dentist.phone || "N/A"}</td>
                <td>{dentist.email}</td>
                <td>{dentist.specialization || "General"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p className="muted">No dentists loaded yet. Submit the form or load the existing directory.</p>
      )}
    </section>
  );
}
