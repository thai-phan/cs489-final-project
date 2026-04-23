import { createContext, useContext, useMemo, useState } from "react";
import {apiBaseUrl} from "../api/http.js";
import axios from "axios";
import {useNavigate} from "react-router-dom";

const AuthContext = createContext(null);
const TOKEN_KEY = "ads_auth_token";
const EMAIL_KEY = "ads_auth_email";
const ROLES_KEY = "ads_auth_roles";

export const axiosApi = axios.create({
  baseURL: apiBaseUrl,
  withCredentials: true,
});

// Add token dynamically (important!)
axiosApi.interceptors.request.use((config) => {
  const token = localStorage.getItem("token"); // or cookie / store
  if (token) {
    console.log("Adding auth token to request if available...");

    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY));
  const [email, setEmail] = useState(() => localStorage.getItem(EMAIL_KEY));
  const [roles, setRoles] = useState(() => {
    const savedRoles = localStorage.getItem(ROLES_KEY);
    return savedRoles ? JSON.parse(savedRoles) : [];
  });
  const navigate = useNavigate();


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
    // redirect to home page or login page if needed
    navigate("/", { replace: true });

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
