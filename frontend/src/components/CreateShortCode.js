import React, { useEffect, useRef } from 'react';

const CreateShortCode = ({
  handleCreateCode,
  createCode,
  setCreateCode,
  setShowCreateShortCode
}) => {
  const modalRef = useRef();

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (modalRef.current && !modalRef.current.contains(event.target)) {
        setShowCreateShortCode(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [setShowCreateShortCode]);

  const handleSubmit = (e) => {
    e.preventDefault();
    handleCreateCode(); // samo pozivanje funkcije koja će setovati shortCode u App
    setShowCreateShortCode(false); // zatvori modal
  };

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      width: '100vw',
      height: '100vh',
      backgroundColor: 'rgba(0, 0, 0, 0.5)',
      zIndex: 1,
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
    }}>
      <div
        ref={modalRef}
        style={{
          padding: '20px',
          backgroundColor: 'white',
          border: '1px solid #ccc',
          borderRadius: '8px',
          boxShadow: '0 4px 10px rgba(0,0,0,0.3)',
          height: '40%',
          width: '30%'
        }}
      >
        <h2>Create short code</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Create short code"
            value={createCode}
            onChange={(e) => setCreateCode(e.target.value)}
          />
          <br />
          <button >Confirm</button>
        </form>
      </div>
    </div>
  );
};

export default CreateShortCode;