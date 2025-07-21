import React, { useState } from 'react';
import axios from 'axios';

const AdminPanel = ({ token }) => {
  const [output, setOutput] = useState("");
  const [id, setId] = useState("");

  const headers = {
    Authorization: `Bearer ${token}`,
  };

 const fetchAllUrls = async () => {
   try {
     const res = await axios.get("http://localhost:8080/admin/urls", { headers });
     setOutput(JSON.stringify(res.data, null, 2));
   } catch (error) {
     console.error("Greška pri dohvaćanju URL-ova:", error);
     setOutput("Greška: " + (error.response?.data?.message || error.message));
   }
 };

  const fetchAllUsers = async () => {
    const res = await axios.get("http://localhost:8080/admin/users", { headers });
    setOutput(JSON.stringify(res.data, null, 2));
  };

  const fetchUrlByCode = async () => {
    const id = prompt("Unesite id URL-a:");
    if (id) {
      try {
        const res = await axios.get(`http://localhost:8080/admin/urls/${id}`, { headers });
        setOutput(JSON.stringify(res.data, null, 2));
      } catch (error) {
        setOutput("Greška: " + (error.response?.data?.message || error.message));
      }
    }
  };

  const fetchUserById = async () => {
    const id = prompt("Unesite ID korisnika:");
    if (id) {
      const res = await axios.get(`http://localhost:8080/admin/users/${id}`, { headers });
      setOutput(JSON.stringify(res.data, null, 2));
    }
  };

  const deleteUserById = async () => {
    const id = prompt("Unesite ID korisnika za brisanje:");
    if (id) {
      await axios.delete(`http://localhost:8080/admin/users/${id}`, { headers });
      setOutput(`Korisnik sa ID ${id} je obrisan.`);
    }
  };

  const deleteUrlByCode = async () => {
    const id = prompt("Unesite id URL-a za brisanje:");
    if (id) {
      try {
        await axios.delete(`http://localhost:8080/admin/urls/${id}`, { headers });
        setOutput(`URL sa id '${id}' je obrisan.`);
      } catch (error) {
        setOutput("Greška: " + (error.response?.data?.message || error.message));
      }
    }
  };

  const fetchHealth = async () => {
     try {
       const res = await axios.get("http://localhost:8080/actuator/health", { headers });
       setOutput(JSON.stringify(res.data, null, 2));
     } catch (error) {
       console.error("Greška pri dohvaćanju Healtha:", error);
       setOutput("Greška: " + (error.response?.data?.message || error.message));
     }
   };

   const fetchMetrics = async () => {
        try {
          const res = await axios.get("http://localhost:8080/actuator/metrics", { headers });
          setOutput(JSON.stringify(res.data, null, 2));
        } catch (error) {
          console.error("Greška pri dohvaćanju Metricsa:", error);
          setOutput("Greška: " + (error.response?.data?.message || error.message));
        }
      };

      const fetchCaches = async () => {
           try {
             const res = await axios.get("http://localhost:8080/actuator/caches", { headers });
             setOutput(JSON.stringify(res.data, null, 2));
           } catch (error) {
             console.error("Greška pri dohvaćanju Caches:", error);
             setOutput("Greška: " + (error.response?.data?.message || error.message));
           }
         };

         const fetchLoggers = async () => {
              try {
                const res = await axios.get("http://localhost:8080/actuator/loggers", { headers });
                setOutput(JSON.stringify(res.data, null, 2));
              } catch (error) {
                console.error("Greška pri dohvaćanju Loggersa:", error);
                setOutput("Greška: " + (error.response?.data?.message || error.message));
              }
            };

  return (
    <div style={{ padding: "20px", border: "1px solid gray", margin: "20px" }}>
      <h2>Admin Panel</h2>
      <button onClick={fetchAllUrls}>Prikaži sve URL-ove</button>
      <button onClick={fetchAllUsers}>Prikaži sve korisnike</button>
      <button onClick={fetchUrlByCode}>Pronađi URL po kodu</button>
      <button onClick={fetchUserById}>Pronađi korisnika po ID-u</button>
      <button onClick={deleteUserById}>Obriši korisnika po ID-u</button>
      <button onClick={deleteUrlByCode}>Obriši URL po kodu</button>

      <button onClick={fetchHealth}>Health</button>
      <button onClick={fetchMetrics}>Metrics</button>
      <button onClick={fetchCaches}>Caches</button>
      <button onClick={fetchLoggers}>Loggers</button>
      <pre style={{ backgroundColor: "#eee", padding: "10px", marginTop: "20px", whiteSpace: "pre-wrap" }}>
        {output}
      </pre>
    </div>
  );
};

export default AdminPanel;