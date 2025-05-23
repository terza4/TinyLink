import React from 'react';

const HistoryList = ({ history }) => (
  <div style={{
    position: 'fixed',
    top: '10%',
    left: '1%',
    width: '30vw', //sirina
    height: '100vh',
    backgroundColor: '#fff',
    border: '2px',
    borderRadius: '8px',
    padding: '10px',
    overflowY: 'auto',
    zIndex: 2, // da bude iznad ostalog
    boxShadow: '-2px 0 8px rgba(0,0,0,0.1)',
  }}>
    <h3 style={{ marginTop: 0, textAlign: 'center' }}>Your Links</h3>
    <ul style={{ listStyleType: 'none', paddingLeft: 0 }}>
      {history.map((item, index) => (
        <li key={index} style={{ marginBottom: '8px' }}>
          <a href={item.shortUrl} target="_blank" rel="noopener noreferrer">
            {item.shortUrl}
          </a>
        </li>
      ))}
    </ul>
  </div>
);

export default HistoryList;


