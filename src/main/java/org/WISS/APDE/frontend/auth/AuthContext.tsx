import React, {createContext, useCallback, useEffect, useMemo, useState} from "react";

type AuthContextType = {
  token: string | null;
  roles: string[];
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
  apiFetch: (path: string, init?: RequestInit) => Promise<Response>;
  apiBase: string;
};

export const AuthContext = createContext<AuthContextType | null>(null);

const decodeRoles = (jwt: string): string[] => {
  try {
    const payload = JSON.parse(atob(jwt.split(".")[1]));
    const roles = payload?.roles ?? [];
    return Array.isArray(roles) ? roles : [];
  } catch {
    return [];
  }
};

export const AuthProvider: React.FC<{children: React.ReactNode}> = ({ children }) => {
  const API_BASE = (import.meta as any).env?.VITE_API_URL ?? "http://localhost:8080";
  const [token, setToken] = useState<string | null>(() => localStorage.getItem("token"));
  const [roles, setRoles] = useState<string[]>(() => token ? decodeRoles(token) : []);

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
      setRoles(decodeRoles(token));
    } else {
      localStorage.removeItem("token");
      setRoles([]);
    }
  }, [token]);

  const login = useCallback(async (username: string, password: string) => {
    const res = await fetch(`${API_BASE}/api/v1/auth/login`, {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify({username, password})
    });
    if (!res.ok) throw new Error("Login fehlgeschlagen");
    const { token } = await res.json();
    setToken(token);
  }, [API_BASE]);

  const logout = useCallback(() => setToken(null), []);

  const apiFetch: AuthContextType["apiFetch"] = useCallback(async (path, init={}) => {
    const headers = new Headers(init.headers || {});
    if (!headers.has("Content-Type") && init.body) headers.set("Content-Type", "application/json");
    if (token) headers.set("Authorization", `Bearer ${token}`);
    return fetch(`${API_BASE}${path}`, {...init, headers});
  }, [API_BASE, token]);

  const value = useMemo(() => ({ token, roles, login, logout, apiFetch, apiBase: API_BASE }), [token, roles, login, logout, apiFetch, API_BASE]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
