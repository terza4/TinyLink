import React from 'react';

const UrlForm = ({ longUrl, setLongUrl, onSubmit, isValid, shortCode, setShowCreateShortCode }) => {
  return (
    <form onSubmit={onSubmit}>
      <input
        type="text"
        placeholder="Enter the long URL"
        value={longUrl}
        onChange={(e) => setLongUrl(e.target.value)}
      />
      <button type="submit" disabled={!isValid(longUrl)}>Shorten</button>
      <button type="button" onClick={() => { setShowCreateShortCode(true); }}> Create yours short code</button>
    </form>
  );
};

export default UrlForm;
