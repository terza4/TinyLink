import React, { useContext, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthContext } from "./context/AuthContext";
import axios from 'axios';
import UrlForm from './components/UrlForm';
import ShortenedResult from './components/ShortenedResult';
import Navbar from './components/Navbar';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import HistoryList from './components/HistoryList';
import CreateShortCode from './components/CreateShortCode';
import CreateExpiryDate from './components/CreateExpiryDate';
import { jwtDecode } from "jwt-decode";
import AdminPanel from './components/AdminPanel';
import './App.css';

function App() {
  const { token, login, logout, isAdmin } = useContext(AuthContext);
  const [showLogin, setShowLogin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);
  const [showHistory, setShowHistory] = useState(false);
  const [longUrl, setLongUrl] = useState('');
  const [shortUrl, setShortUrl] = useState('');
  const [error, setError] = useState('');
  const [registerUsername, setRegisterUsername] = useState("");
  const [registerPassword, setRegisterPassword] = useState("");
  const [loginUsername, setLoginUsername] = useState("");
  const [loginPassword, setLoginPassword] = useState("");
  const [shortCode, setShortCode] = useState("");
  const [showCreateShortCode, setShowCreateShortCode] = useState(false);
  const [createCode, setCreateCode] = useState("");

  const [expiryDate, setExpiryDate] = useState("");
  const [showCreateExpiryDate, setShowCreateExpiryDate] = useState(false);
  const [createExpiryDate, setCreateExpiryDate] = useState("");
  const [setIsAdmin] = useState(false);
  const [showAdminPanel, setShowAdminPanel] = useState(false);
  const [history, setHistory] = useState([]);

  //funckija koja dodaje shortCode u App.js
  const handleCreateCode = () => {
    setShortCode(createCode); // ubacivanje koda iz modala u glavni shortCode state
    setCreateCode(""); // restart unosa
  };
 //funkcija koja dodaje datum isteka
  const handleExpiryDate = () => {
      setExpiryDate(createExpiryDate); // ubacivanje datuma iz modala u glavni expiryDate state
      setCreateExpiryDate(""); // restart unosa
    };


  // dohvaćanje historije
  const fetchHistory = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/history", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setHistory(response.data);
    } catch (err) {
      console.error("Greška prilikom dohvaćanja historije:", err);
    }
  };

  // registracija
  const handleRegister = async (e) => {
      e.preventDefault();
      try {
        const response = await axios.post("http://localhost:8080/auth/register", {
          username: registerUsername,
          password: registerPassword,
        });
        const newToken = response.data.replace("Bearer ", "");
        login(newToken);
        setShowRegister(false);
        alert("Uspješna registracija!");
      } catch (err) {
        const errorMessage = err.response?.data?.message || err.response?.data || "Greška prilikom registracija";
        alert(errorMessage);
      }
    };



  // login
  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8080/auth/login", {
        username: loginUsername,
        password: loginPassword,
      });

      const newToken = response.data.replace("Bearer ", "");
      login(newToken);
      setShowLogin(false);

      alert("Uspješno prijavljeni!");
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.response?.data || "Greška prilikom logina";
      alert(errorMessage);
    }
  };

  // validacija URL-a
  const isValidUrl = (string) => {
    try {
      new URL(string);
      return true;
    } catch (_) {
      return false;
    }
  };

  // slanje URL-a
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setShortUrl('');

    if (!longUrl.trim()) {
      setError("URL ne smije biti prazan.");
      return;
    }

    if (!isValidUrl(longUrl)) {
      setError("Unesite ispravan URL (npr. https://example.com).");
      return;
    }

    //Pri slanju URL-a provjerava se da li postoji i shortCode ako ne postoji salje se samo longUrl
    const requestBody = { url: longUrl };
    if (shortCode) {
      requestBody.shortCodee = shortCode;
    }
    if(expiryDate){
      requestBody.expiryDate = expiryDate;
    }

    try {
      const response = await axios.post('http://localhost:8080/api/shorten', requestBody, token ? {
        headers: {
          Authorization: `Bearer ${token}`
        }
      } : {});

      setShortUrl(response.data.shortUrl);
      setShortCode(""); // 🧹 reset shortCode nakon slanja
      setExpiryDate("");


          if (token) {
            await fetchHistory();
          }
    } catch (err) {
      setError("Došlo je do greške: " + (err.response?.data?.message || err.message));
    }
  };

  // kopiranje
  const handleCopy = (text) => {
    navigator.clipboard.writeText(text)
      .catch((err) => alert("Greška prilikom kopiranja: " + err));
  };

  //delete linka iz historije
   const handleDelete = async (shortCode) => {
     try {
       await axios.delete(`http://localhost:8080/api/delete/${shortCode}`, {

         headers: {
           Authorization: `Bearer ${token}`
         }
       });

       // osvježavanje liste nakon brisanja
       fetchHistory();
     } catch (error) {
       console.error("Greška pri brisanju linka:", error.response?.data || error.message);
     }
   };




  return (
    <Router>
      <Navbar
        token={token}
        setShowRegister={setShowRegister}
        setShowLogin={setShowLogin}
        setShowHistory={setShowHistory}
        showHistory={showHistory}
        fetchHistory={fetchHistory}
        logout={logout}
        isAdmin={isAdmin}
        setShowAdminPanel={setShowAdminPanel}
      />

      <Routes>
        {/* Početna stranica */}
        <Route
          path="/"
          element={
            <>
              {showLogin && (
                <LoginForm
                  handleLogin={handleLogin}
                  loginUsername={loginUsername}
                  setLoginUsername={setLoginUsername}
                  loginPassword={loginPassword}
                  setLoginPassword={setLoginPassword}
                  setShowLogin={setShowLogin}
                />
              )}

              {showRegister && (
                <RegisterForm
                  handleRegister={handleRegister}
                  registerUsername={registerUsername}
                  setRegisterUsername={setRegisterUsername}
                  registerPassword={registerPassword}
                  setRegisterPassword={setRegisterPassword}
                  setShowRegister={setShowRegister}
                />
              )}

              {showHistory && (
                <HistoryList
                  history={history}
                  onCopy={handleCopy}
                  onDelete={handleDelete}
                />
              )}

              {showCreateShortCode && (
                <CreateShortCode
                  handleCreateCode={handleCreateCode}
                  createCode={createCode}
                  setCreateCode={setCreateCode}
                  setShowCreateShortCode={setShowCreateShortCode}
                />
              )}

              {showCreateExpiryDate && (
                <CreateExpiryDate
                  handleExpiryDate={handleExpiryDate}
                  createExpiryDate={createExpiryDate}
                  setCreateExpiryDate={setCreateExpiryDate}
                  setShowCreateExpiryDate={setShowCreateExpiryDate}
                />
              )}

              <div className="container">
                <h2>URL Shortener</h2>
                <UrlForm
                  longUrl={longUrl}
                  setLongUrl={setLongUrl}
                  onSubmit={handleSubmit}
                  isValid={isValidUrl}
                  shortCode={shortCode}
                  setShowCreateShortCode={setShowCreateShortCode}
                  expiryDate={expiryDate}
                  setShowCreateExpiryDate={setShowCreateExpiryDate}
                />

                {shortCode && (
                  <p style={{ marginTop: '10px' }}>
                    Using custom short code: <strong>{shortCode}</strong>
                  </p>
                )}

                {expiryDate && (
                  <p style={{ marginTop: '10px' }}>
                    Using custom expiry date: <strong>{expiryDate}</strong>
                  </p>
                )}

                <ShortenedResult shortUrl={shortUrl} onCopy={handleCopy} />
                {error && <p className="error">{error}</p>}
              </div>
            </>
          }
        />

        {/* Admin Panel */}
        <Route path="/admin" element={<AdminPanel token={token} />} />
      </Routes>
    </Router>
  );
  }

export default App;

