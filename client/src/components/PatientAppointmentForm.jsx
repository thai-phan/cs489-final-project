const DENTIST_OPTIONS = [
  "No Preference",
  "Dr. Emily Carter",
  "Dr. Michael Tran",
  "Dr. Sophia Patel",
  "Dr. James Wilson"
];

export default function PatientAppointmentForm({
  form,
  onFieldChange,
  onSubmit,
  onClear,
  error,
  success,
  loading
}) {
  return (
    <div className="card public-form-card">
      <form onSubmit={onSubmit}>
        <div className="form-grid">
          <div className="form-row">
            <label htmlFor="patient-first-name">First Name</label>
            <input
              id="patient-first-name"
              value={form.firstName}
              onChange={(event) => onFieldChange("firstName", event.target.value)}
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="patient-last-name">Last Name</label>
            <input
              id="patient-last-name"
              value={form.lastName}
              onChange={(event) => onFieldChange("lastName", event.target.value)}
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="patient-phone">Contact Phone Number</label>
            <input
              id="patient-phone"
              type="tel"
              value={form.phone}
              onChange={(event) => onFieldChange("phone", event.target.value)}
              placeholder="(515) 555-0142"
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="patient-email">Email</label>
            <input
              id="patient-email"
              type="email"
              value={form.email}
              onChange={(event) => onFieldChange("email", event.target.value)}
              placeholder="patient@example.com"
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="preferred-dentist">Preferred Dentist</label>
            <select
              id="preferred-dentist"
              value={form.preferredDentist}
              onChange={(event) => onFieldChange("preferredDentist", event.target.value)}
              required
            >
              <option value="">Select dentist</option>
              {DENTIST_OPTIONS.map((dentist) => (
                <option key={dentist} value={dentist}>
                  {dentist}
                </option>
              ))}
            </select>
          </div>
          <div className="form-row">
            <label htmlFor="contact-method">Preferred Follow-Up</label>
            <select
              id="contact-method"
              value={form.contactMethod}
              onChange={(event) => onFieldChange("contactMethod", event.target.value)}
              required
            >
              <option value="Phone Call">Phone Call</option>
              <option value="Email">Email</option>
            </select>
          </div>
          <div className="form-row">
            <label htmlFor="appointment-date">Preferred Date</label>
            <input
              id="appointment-date"
              type="date"
              value={form.appointmentDate}
              onChange={(event) => onFieldChange("appointmentDate", event.target.value)}
              required
            />
          </div>
          <div className="form-row">
            <label htmlFor="appointment-time">Preferred Time</label>
            <input
              id="appointment-time"
              type="time"
              value={form.appointmentTime}
              onChange={(event) => onFieldChange("appointmentTime", event.target.value)}
              required
            />
          </div>
        </div>

        <div className="form-row">
          <label htmlFor="reason">Reason for Visit</label>
          <textarea
            id="reason"
            className="text-area"
            value={form.reason}
            onChange={(event) => onFieldChange("reason", event.target.value)}
            placeholder="Cleaning, tooth pain, consultation, follow-up, or another reason."
            rows="4"
            required
          />
        </div>

        <div className="form-actions">
          <button className="btn-primary" type="submit" disabled={loading}>
            {loading ? "Submitting..." : "Submit Request"}
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
