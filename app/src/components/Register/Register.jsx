import axios from 'axios';
import { useFormik } from 'formik'
import React, { useState } from 'react'
import { RotatingLines } from 'react-loader-spinner';
import { useNavigate } from 'react-router-dom';


export default function Register() {

    let user = {
        username: "",
        email: "",
        password: ""
    }

    const [registerMesage, setregisterMesage] = useState(null)
    const [isLoding, setisLoding] = useState(false)

    const navigateToLogin = useNavigate()



    async function registerNewUser(values) {

        setisLoding(true)

        try {
            const data = await axios.post("http://localhost:8080/api/auth/register", values)
            // console.log(data);

            if (data.status === 200) {
                setregisterMesage("success")           
                setTimeout(function () {
                    navigateToLogin("/login")
                }, 1000)
            }
        } catch (error) {
            console.log("error in register", error);
            setregisterMesage("Username already exists")
        }
        setisLoding(false)

    }



    const formikObj = useFormik({
        initialValues: user,
        onSubmit: registerNewUser,

        validate: function (values) {
            // console.log("validate");
            setregisterMesage(null)

            const errors = {}

            if (values.username.length < 4 || values.username.length > 10) {
                errors.username = "Name must be from 4 characters to 10 characters "
            }

            if (values.email.includes("@") === false || values.email.includes(".") === false) {
                errors.email = "email invalid"
            }

            
            if (values.password.length < 6 || values.password.length > 12) {
                errors.password = "password must be from 6 characters to 12 characters "
            }
            // console.log(errors);
            return errors;
        }

    });

    return <>
        <div className="w-75 m-auto py-5">

            {registerMesage != null ? <div className="alert alert-success">{registerMesage} </div> : ""}
            <h2> Register now : </h2>
            <form onSubmit={formikObj.handleSubmit} >

                <label htmlFor="username">username : </label>
                <input onBlur={formikObj.handleBlur} onChange={formikObj.handleChange} value={formikObj.values.username} id='username' type="text" placeholder='username' className='form-control mb-3' />
                {formikObj.errors.username && formikObj.touched.username ? <div className="alert alert-danger"> {formikObj.errors?.username}</div> : ""}


                <label htmlFor="email">Email : </label>
                <input onBlur={formikObj.handleBlur} onChange={formikObj.handleChange} value={formikObj.values.email} id='email' type="text" placeholder='email' className='form-control mb-3' />
                {formikObj.errors.email && formikObj.touched.email ? <div className="alert alert-danger"> {formikObj.errors?.email}</div> : ""}


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
                /> : "Register"}
                </button>


            </form>
        </div>



    </>
}
