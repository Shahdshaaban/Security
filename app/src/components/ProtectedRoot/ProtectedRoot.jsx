import React, { useContext } from 'react'
import Login from '../Login/Login';
import { Navigate } from 'react-router-dom';
import { authContext } from '../Context/authenticationContext';

export default function ProtectedRoot({ children }) {
  const { token } = useContext(authContext)


  if (token === null) {

    return <Navigate to="/login" />;
  }


  return <>

  

    {children}

  </>
}
