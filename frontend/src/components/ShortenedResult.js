import React from 'react';

const ShortenedResult = ({ shortUrl, onCopy }) => {
  if (!shortUrl) return null;

  return (
    <div className="result">
      <p>
        Shortened URL: <a href={shortUrl} target="_blank" rel="noreferrer">{shortUrl}</a>
        <button onClick={() => onCopy(shortUrl)} style={{ marginLeft: '10px' }}>Copy</button>
      </p>
    </div>
  );
};

export default ShortenedResult;