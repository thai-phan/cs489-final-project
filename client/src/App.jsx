import { Link, Navigate, Route, Routes } from "react-router-dom";
import { useAuth } from "./auth/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import DashboardPage from "./pages/DashboardPage";
import PatientAppointmentPage from "./pages/PatientAppointmentPage";
import PatientInformationPage from "./pages/PatientInformationPage";
import MyAppointmentsPage from "./pages/MyAppointmentsPage";
import WelcomePage from "./pages/WelcomePage";

export default function App() {
  const { token, logout, email, roles } = useAuth();

  const isManager = roles.includes("ROLE_MANAGER");
  const isDentist = roles.includes("ROLE_DENTIST");
  const isPatient = roles.includes("ROLE_PATIENT");

  return (
    <div className="app-container">
      <header className="top-nav">
        <h1>Advantis Dental Surgeries</h1>
        <nav>
          {token && <Link to="/my-appointments">My Appointments</Link>}
          {(isManager || isDentist) && <Link to="/dashboard">Dashboard</Link>}
          {!token && <Link to="/login">Login</Link>}
          {!token && <Link to="/register">Register</Link>}
          {token && <span className="user-badge">{email}</span>}
          {token && <button onClick={logout}>Logout</button>}
        </nav>
      </header>

      <main>
        <Routes>
          <Route path="/" element={<WelcomePage />} />
          <Route path="/patient-information" element={<PatientInformationPage />} />
          <Route
            path="/my-appointments"
            element={
              <ProtectedRoute>
                <MyAppointmentsPage />
              </ProtectedRoute>
            }
          />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute allowedRoles={["ROLE_MANAGER", "ROLE_DENTIST"]}>
                <DashboardPage />
              </ProtectedRoute>
            }
          />
          <Route path="/appointments" element={<PatientAppointmentPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </div>
  );
}
