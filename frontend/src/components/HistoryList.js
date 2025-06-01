import React from 'react';

const HistoryList = ({ history, onCopy, onDelete }) => {
  // Funkcija koja izdvaja shortCode iz shortUrl
  const extractShortCode = (shortUrl) => {
    const parts = shortUrl.split('/');
    return parts[parts.length - 1];
  };

  return (
    <div style={{
      position: 'fixed',
      top: '10%',
      left: '1%',
      width: '30vw',
      height: '100vh',
      backgroundColor: '#fff',
      border: '2px solid #ccc',
      borderRadius: '8px',
      padding: '10px',
      overflowY: 'auto',
      zIndex: 2,
      boxShadow: '-2px 0 8px rgba(0,0,0,0.1)',
    }}>
      <h3 style={{ marginTop: 0, textAlign: 'center' }}>Your Links</h3>
      <ul style={{ listStyleType: 'none', paddingLeft: 0 }}>
        {history.map((item, index) => (
          <li
            key={index}
            style={{
              justifyContent: "space-between",
              marginBottom: '12px',
              display: 'flex',
              alignItems: 'center'
            }}
          >
            {/* Link */}
            <a href={item.shortCode} target="_blank" rel="noopener noreferrer">
              {item.shortCode}
            </a>


            {/* Dugmad sa desne strane */}
            <div style={{ display: 'flex', gap: '5px' }}>
              <p>{item.clickCount}</p>
              <button onClick={() => onCopy(item.shortCode)}>Copy</button>
              <button onClick={() => onDelete(extractShortCode(item.shortCode))}>Delete</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default HistoryList;



