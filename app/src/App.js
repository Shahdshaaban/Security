import React, { Component } from 'react'
import Home from './components/Home/Home';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Layout from './components/Layout/Layout';
import Register from './components/Register/Register';
import NotFound from './components/NotFound/NotFound';
import Login from './components/Login/Login';
import ProtectedRoot from './components/ProtectedRoot/ProtectedRoot';
import { AuthProvider } from './components/Context/authenticationContext';
import Profile from './components/Profile/Profile';


const router = createBrowserRouter([
  {
    path: '', element: <Layout />, children: [
      { path: 'home', element: <ProtectedRoot><Home /></ProtectedRoot>},
      { path: '/', element:<ProtectedRoot><Home /></ProtectedRoot>  },
      // {index: true, element: <Home /> },
      { path: 'register', element: <Register /> },
      { path: 'profile', element:<ProtectedRoot> <Profile /> </ProtectedRoot> },
      { path: 'login', element: <Login /> },
      { path: '*', element: <NotFound /> },
   
    ]
  },
])


export default class App extends Component {
  render() {

    return <>
    <AuthProvider>

      <RouterProvider router={router} />   

    </AuthProvider>
    </>

  }

}

