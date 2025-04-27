import axios from 'axios';
import { useFormik } from 'formik'
import React, { useContext, useState } from 'react'
import { RotatingLines } from 'react-loader-spinner';
import { useNavigate } from 'react-router-dom';
import { authContext } from '../Context/authenticationContext';


export default function Login() {

    let user = {
        username: "",
        password: ""
    }

    const { settoken } = useContext(authContext)

    const [errorInLogin, seterrorInLogin] = useState(null)
    const [isLoding, setisLoding] = useState(false)

    const navigateToLogin = useNavigate()



    async function registerNewUser(values) {

        setisLoding(true)

        try {
            const data = await axios.post("http://localhost:8080/api/auth/login", values)
            console.log(data);

            if (data.status == 200) {


                settoken(data.data.jwtToken)
                localStorage.setItem("token", data.data.jwtToken);

                setTimeout(function () {
                    navigateToLogin("/home")
                }, 1000)
            }

        } catch (error) {
            console.log("error in login", error);
            seterrorInLogin("paassword or username is incorrect")
        }
        setisLoding(false)

    }



    const formikObj = useFormik({
        initialValues: user,
        onSubmit: registerNewUser,

        validate: function (values) {

            seterrorInLogin(null)

            const errors = {}

            if (values.username.length < 4 || values.username.length > 10) {
                errors.username = "Name must be from 4 characters to 10 characters "
            }

            if (values.password.length < 6 || values.password.length > 12) {
                errors.password = "password must be from 6 characters to 12 characters "
            }

            return errors;
        }

    });

    return <>
        <div className="w-75 m-auto py-5">

            {errorInLogin != null ? <div className="alert alert-success">{errorInLogin} </div> : ""}
            <h2> Login now : </h2>
            <form onSubmit={formikObj.handleSubmit} >



                <label htmlFor="username">username : </label>
                <input onBlur={formikObj.handleBlur} onChange={formikObj.handleChange} value={formikObj.values.username} id='username' type="text" placeholder='username' className='form-control mb-3' />
                {formikObj.errors.username && formikObj.touched.username ? <div className="alert alert-danger"> {formikObj.errors?.username}</div> : ""}


                <label htmlFor="password">Password : </label>
                <input onBlur={formikObj.handleBlur} onChange={formikObj.handleChange} value={formikObj.values.password} id='password' type="password" placeholder='password' className='form-control mb-3' />
                {formikObj.errors?.password && formikObj.touched.password ? <div className="alert alert-danger"> {formikObj.errors?.password}</div> : ""}

                <button type="submit" disabled={formikObj.isValid === false || formikObj.dirty === false} className='btn btn-success' > {isLoding ? <RotatingLines
                    visible={true}
                    height="45"
                    width="45"
                    color="white"
                    strokeWidth="5"
                    animationDuration="0.75"
                    ariaLabel="rotating-lines-loading"
                    wrapperStyle={{}}
                    wrapperClass=""
                /> : "Login"}
                </button>

            </form>

        </div>



    </>
}
