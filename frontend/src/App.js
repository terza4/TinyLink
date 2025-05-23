import React, { useContext, useState } from 'react';
import { AuthContext } from "./context/AuthContext";
import axios from 'axios';
import UrlForm from './components/UrlForm';
import ShortenedResult from './components/ShortenedResult';
import Navbar from './components/Navbar';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import HistoryList from './components/HistoryList';
import './App.css';

function App() {
  const { token, login, logout } = useContext(AuthContext);
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
  const [history, setHistory] = useState([]);

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

    try {
      const response = await axios.post('http://localhost:8080/api/shorten', {
        url: longUrl
      }, token ? {
        headers: {
          Authorization: `Bearer ${token}`
        }
      } : {});

      setShortUrl(response.data.shortUrl);


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
    <>
      {/* Dugmadi Register, Login, History, Logout */}
      <Navbar
        token={token}
        setShowRegister={setShowRegister}
        setShowLogin={setShowLogin}
        setShowHistory={setShowHistory}
        showHistory={showHistory}
        fetchHistory={fetchHistory}
        logout={logout}
      />

      {/* Login forma */}
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

      {/* Register forma */}
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

      {/* Historija linkova */}
      {showHistory && <HistoryList history={history}  onCopy={handleCopy} onDelete={handleDelete}/>}

      {/* URL Shortener forma */}
      <div className="container">
        <h2>URL Shortener</h2>
        <UrlForm
          longUrl={longUrl}
          setLongUrl={setLongUrl}
          onSubmit={handleSubmit}
          isValid={isValidUrl}
        />
        <ShortenedResult shortUrl={shortUrl} onCopy={handleCopy} />
        {error && <p className="error">{error}</p>}
      </div>
    </>
  );
}

export default App;

