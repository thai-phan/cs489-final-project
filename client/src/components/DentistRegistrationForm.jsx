const SPECIALIZATIONS = [
  "General Dentistry",
  "Orthodontics",
  "Pediatric Dentistry",
  "Periodontics",
  "Endodontics",
  "Oral and Maxillofacial Surgery",
  "Prosthodontics"
];

export default function DentistRegistrationForm({
  form,
  onFieldChange,
  onSubmit,
  onClear,
  formError,
  formSuccess
}) {
  return (
    <section className="card form-card">
      <div className="section-heading">
        <h2>Register Dentist</h2>
        <p className="muted">
          Office Managers can record each dentist&apos;s ID, contact details, and specialization.
        </p>
      </div>

      <form onSubmit={onSubmit}>
        <div className="form-grid">
          <div className="form-row">
            <label htmlFor="dentist-id">Dentist ID</label>
            <input
              id="dentist-id"
              value={form.dentistId}
              onChange={(event) => onFieldChange("dentistId", event.target.value)}
              placeholder="1006"
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="specialization">Specialization</label>
            <select
              id="specialization"
              value={form.specialization}
              onChange={(event) => onFieldChange("specialization", event.target.value)}
              required
            >
              <option value="">Select specialization</option>
              {SPECIALIZATIONS.map((specialization) => (
                <option key={specialization} value={specialization}>
                  {specialization}
                </option>
              ))}
            </select>
          </div>
          <div className="form-row">
            <label htmlFor="first-name">First Name</label>
            <input
              id="first-name"
              value={form.firstName}
              onChange={(event) => onFieldChange("firstName", event.target.value)}
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="last-name">Last Name</label>
            <input
              id="last-name"
              value={form.lastName}
              onChange={(event) => onFieldChange("lastName", event.target.value)}
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="phone">Contact Phone Number</label>
            <input
              id="phone"
              type="tel"
              value={form.phone}
              onChange={(event) => onFieldChange("phone", event.target.value)}
              placeholder="(515) 555-0199"
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              value={form.email}
              onChange={(event) => onFieldChange("email", event.target.value)}
              placeholder="dentist@advantis.example"
              required
            />
          </div>
        </div>

        <div className="form-actions">
          <button className="btn-primary" type="submit">
            Register Dentist
          </button>
          <button className="btn-secondary" type="button" onClick={onClear}>
            Clear Form
          </button>
        </div>

        {formError && <p className="error">{formError}</p>}
        {formSuccess && <p className="success">{formSuccess}</p>}
      </form>
    </section>
  );
}
