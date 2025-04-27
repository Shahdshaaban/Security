import React, { useContext, useEffect, useState } from 'react';
import { authContext } from '../Context/authenticationContext';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

export default function Profile() {
    const { token } = useContext(authContext);
    const [userName, setUserName] = useState(null);
    const [email, setEmail] = useState(null);
    const [role, setRole] = useState(null);

    useEffect(() => {
        if (!token) return;

        const data = jwtDecode(token);
        const fetchUserInfo = async () => {
            try {
                const response = await axios.get("http://localhost:8080/api/auth/user", {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                const { username, email, role } = response.data;
                setUserName(username);
                setEmail(email);
                setRole(role);
            } catch (error) {
                console.error("Error fetching user info:", error);
            }
        };

        fetchUserInfo();
    }, [token]);

    return (
        <>
            <div style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "80vh",
                backgroundColor: "#eef2f3"
            }}>
                <div style={{
                    padding: "30px",
                    borderRadius: "15px",
                    boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                    backgroundColor: "#fff",
                    textAlign: "center",
                    width: "500px"
                }}>
                    <h1>Welcome, {userName}</h1>
                    <p>Email: {email}</p>
                    <p>Role: {role}</p>
                </div>
            </div>

        </>
    );
}
