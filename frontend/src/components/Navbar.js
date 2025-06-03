import React from 'react';
import { useNavigate } from 'react-router-dom';

const Navbar = ({ token, isAdmin, setShowRegister, setShowLogin, setShowHistory, showHistory, fetchHistory, logout, setShowAdminPanel }) => {
  const navigate = useNavigate();

  return (
    <div style={{ display: "flex", justifyContent: "space-between", padding: "10px" }}>
      <div>
        {!token ? (
          <>
            <button onClick={() => { setShowRegister(true); setShowLogin(false); }}>Register</button>
            <button onClick={() => { setShowLogin(true); setShowRegister(false); }}>Login</button>
          </>
        ) : (
          <>
            <button onClick={() => {
              setShowHistory(!showHistory);
              if (!showHistory) fetchHistory();
            }}>History</button>
            {isAdmin && (
              <button onClick={() => navigate("/admin")}>Admin Panel</button>
            )}
          </>
        )}
      </div>
      {token && (
        <button onClick={() => { logout(); setShowHistory(false); }}>Logout</button>
      )}
    </div>
  );
};

export default Navbar;
