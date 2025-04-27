import { createContext, useEffect, useState } from "react";


export const authContext =createContext();

export function AuthProvider({children}){

    const [token, settoken] = useState(null)

   

    useEffect(function(){

         if (localStorage.getItem("token") !== null) {
        settoken(localStorage.getItem("token"))
        
         }

    },[])
  

    return  <authContext.Provider value={{ token  ,  settoken }}>
            {children}
        </authContext.Provider>
    
}

