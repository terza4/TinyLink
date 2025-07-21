import { createContext, useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);

  // Helper funkcija za proveru admin uloge
  const checkIfAdmin = (jwtToken) => {
    try {
      const decoded = jwtDecode(jwtToken);
      const roles = decoded.roles || [];
      return roles.includes("ROLE_ADMIN");
    } catch (error) {
      console.error("Greška pri dekodiranju tokena:", error);
      return false;
    }
  };

  useEffect(() => {
    const savedToken = localStorage.getItem("token");
    if (savedToken) {
      setToken(savedToken);
      setIsAdmin(checkIfAdmin(savedToken)); // ✔️ proveri admin ulogu
    }
  }, []);

  const login = (newToken) => {
    localStorage.setItem("token", newToken);
    setToken(newToken);
    setIsAdmin(checkIfAdmin(newToken)); // ✔️ proveri admin ulogu
  };

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    setIsAdmin(false);
  };

  return (
    <AuthContext.Provider value={{ token, login, logout, isAdmin, setIsAdmin }}>
      {children}
    </AuthContext.Provider>
  );
};