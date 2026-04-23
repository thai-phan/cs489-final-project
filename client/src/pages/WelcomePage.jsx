import {useAuth} from "../auth/AuthContext.jsx";

export default function WelcomePage() {
  const {token, email, roles} = useAuth()

  return (
      <div className="welcome-page">
        <section className="hero-section">
          <div className="hero-content">
            <h1 className="hero-title">Welcome to Advantis Dental Surgeries</h1>
            <p className="hero-subtitle">
              Your trusted partner for comprehensive dental care. We provide exceptional services with a focus on
              patient comfort and modern technology.
            </p>
            <div className="hero-buttons">
              {
                token ?
                    <a href="/book-appointment" className="btn btn-primary">Schedule Appointment</a>
                    :
                    <a href="/login" className="btn btn-secondary">Sign In</a>
              }
            </div>
          </div>
          <div className="hero-image">
            <div className="placeholder-image">
              <span>🦷</span>
              <p>Dental Care</p>
            </div>
          </div>
        </section>

        <section className="features-section">
          <div className="container">
            <h2>Why Choose Us?</h2>
            <div className="features-grid">
              <div className="feature-card">
                <div className="feature-icon">👨‍⚕️</div>
                <h3>Expert Dentists</h3>
                <p>Our team of experienced professionals provides top-quality care.</p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">🛡️</div>
                <h3>Safe & Modern</h3>
                <p>State-of-the-art equipment and strict safety protocols.</p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">📅</div>
                <h3>Easy Scheduling</h3>
                <p>Book appointments online with our convenient system.</p>
              </div>
              <div className="feature-card">
                <div className="feature-icon">❤️</div>
                <h3>Patient-Centered</h3>
                <p>Your comfort and satisfaction are our top priorities.</p>
              </div>
            </div>
          </div>
        </section>

        <section className="cta-section">
          <div className="container">
            <h2>Ready to Get Started?</h2>
            <p>Join thousands of satisfied patients who trust us with their dental health.</p>
            <a href="/patient-information" className="btn btn-primary">Learn More About Our Services</a>
          </div>
        </section>
      </div>
  );
}
