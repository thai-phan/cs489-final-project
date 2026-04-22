import { createContext, useContext, useMemo, useState } from "react";

const AuthContext = createContext(null);
const TOKEN_KEY = "ads_auth_token";
const EMAIL_KEY = "ads_auth_email";
const ROLES_KEY = "ads_auth_roles";

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY));
  const [email, setEmail] = useState(() => localStorage.getItem(EMAIL_KEY));
  const [roles, setRoles] = useState(() => {
    const savedRoles = localStorage.getItem(ROLES_KEY);
    return savedRoles ? JSON.parse(savedRoles) : [];
  });

  const login = (nextToken, nextEmail, nextRoles = []) => {
    setToken(nextToken);
    setEmail(nextEmail);
    setRoles(nextRoles);
    localStorage.setItem(TOKEN_KEY, nextToken);
    localStorage.setItem(EMAIL_KEY, nextEmail);
    localStorage.setItem(ROLES_KEY, JSON.stringify(nextRoles));
  };

  const logout = () => {
    setToken(null);
    setEmail(null);
    setRoles([]);
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(EMAIL_KEY);
    localStorage.removeItem(ROLES_KEY);
  };

  const value = useMemo(
    () => ({ token, email, roles, login, logout }),
    [token, email, roles]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return context;
}
