export default function PatientInformationForm({
  form,
  onFieldChange,
  onSubmit,
  onClear,
  error,
  success
}) {
  return (
    <div className="card public-form-card">
      <form onSubmit={onSubmit}>
        <div className="form-grid">
          <div className="form-row">
            <label htmlFor="info-first-name">First Name</label>
            <input
              id="info-first-name"
              value={form.firstName}
              onChange={(event) => onFieldChange("firstName", event.target.value)}
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="info-last-name">Last Name</label>
            <input
              id="info-last-name"
              value={form.lastName}
              onChange={(event) => onFieldChange("lastName", event.target.value)}
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="info-phone">Contact Phone Number</label>
            <input
              id="info-phone"
              type="tel"
              value={form.phone}
              onChange={(event) => onFieldChange("phone", event.target.value)}
              placeholder="(515) 555-0160"
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="info-email">Email</label>
            <input
              id="info-email"
              type="email"
              value={form.email}
              onChange={(event) => onFieldChange("email", event.target.value)}
              placeholder="patient@example.com"
              required
            />
          </div>
          <div className="form-row form-row--full">
            <label htmlFor="info-address">Mailing Address</label>
            <textarea
              id="info-address"
              className="text-area"
              value={form.mailingAddress}
              onChange={(event) => onFieldChange("mailingAddress", event.target.value)}
              placeholder="123 Main St, Apt 2B, Des Moines, IA 50309"
              rows="3"
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="info-dob">Date of Birth</label>
            <input
              id="info-dob"
              type="date"
              value={form.dateOfBirth}
              onChange={(event) => onFieldChange("dateOfBirth", event.target.value)}
              required
            />
          </div>
        </div>

        <div className="form-actions">
          <button className="btn-primary" type="submit">
            Save Patient Information
          </button>
          <button className="btn-secondary" type="button" onClick={onClear}>
            Clear Form
          </button>
        </div>

        {error && <p className="error">{error}</p>}
        {success && <p className="success">{success}</p>}
      </form>
    </div>
  );
}
