import React from 'react';

const Navbar = ({ token, setShowRegister, setShowLogin, setShowHistory, showHistory, fetchHistory, logout }) => (
  <div style={{ display: "flex", justifyContent: "space-between", padding: "10px" }}>
    <div>
      {!token ? (
        <>
          <button onClick={() => { setShowRegister(true); setShowLogin(false); }}>Register</button>
          <button onClick={() => { setShowLogin(true); setShowRegister(false); }}>Login</button>
        </>
      ) : (
        <button onClick={() => {
          setShowHistory(!showHistory);
          if (!showHistory) fetchHistory();
        }}>History</button>
      )}
    </div>
    {token && (
      <button onClick={() => { logout(); setShowHistory(false); }}>Logout</button>
    )}
  </div>
);

export default Navbar;
