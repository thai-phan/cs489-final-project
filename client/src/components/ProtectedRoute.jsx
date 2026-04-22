import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function ProtectedRoute({ children, allowedRoles = [] }) {
  const { token, roles } = useAuth();
  if (!token) {
    return <Navigate to="/login" replace />;
  }
  if (allowedRoles.length > 0 && !allowedRoles.some(role => roles.includes(role))) {
    return <Navigate to="/" replace />;
  }
  return children;
}
