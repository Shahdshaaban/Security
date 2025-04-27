import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function Home() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token");

    axios.get("http://localhost:8080/api/auth/user", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then(res => {
      setUser(res.data); 
    })
    .catch(err => {
      console.error("Error fetching profile:", err); 
    });
  }, []);

  return (
    <div className="d-flex justify-content-center flex-column align-items-center py-5">
      {user ? (
        <>
          <h1>Welcome, {user.username}</h1>
          <h4>Email: {user.email}</h4> 
        </>
      ) : (
        <h2>Loading...</h2>  
      )}
    </div>
  );
}
