import React from 'react';

const UrlForm = ({ longUrl, setLongUrl, onSubmit, isValid }) => {
  return (
    <form onSubmit={onSubmit}>
      <input
        type="text"
        placeholder="Enter the long URL"
        value={longUrl}
        onChange={(e) => setLongUrl(e.target.value)}
      />
      <button type="submit" disabled={!isValid(longUrl)}>Shorten</button>
    </form>
  );
};

export default UrlForm;
