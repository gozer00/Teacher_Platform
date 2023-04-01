import Login from "./Login";
import Input from "react-validation/build/input";
import React, {useCallback, useEffect, useState} from "react";
import Form from "react-validation/build/form";
import {useDispatch, useSelector, useStore} from "react-redux";
import {updateUser, getUser} from "../services/user.service";
import {useNavigate} from "react-router-dom";
import {logout} from "../actions/auth";

const ManagePersonalData = () => {

    const navigate = useNavigate();

    const { user: currentUser } = useSelector((state) => state.auth);
    const dispatch = useDispatch();



    const [username, setUsername] = useState();
    const [email, setEmail] = useState();

    useEffect(() => {
        getUser(currentUser.id).then((res) => {
            setUsername(res.data.username);
            setEmail(res.data.email);
        })
    }, [])

    const onChangeUsername = (e) => {
        setUsername(e.target.value);
    }

    const onChangeEmail = (e) => {
        setEmail(e.target.value)
    }

    const handleSave = ()=> {
            let update = {
                "userId": currentUser.id,
                "userName": username,
                "email": email
            }
            updateUser(update);
            dispatch(logout());
            navigate("/login");
        };

    return (
        <div className="col-md-12">
            <div className="card card-container">
                <Form onSubmit={handleSave}>
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <Input
                            type="text"
                            className="form-control"
                            name="username"
                            value={username}
                            onChange={onChangeUsername}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <Input
                            type="text"
                            className="form-control"
                            name="email"
                            value={email}
                            onChange={onChangeEmail}
                        />
                    </div>

                    <div className="form-group">
                        <button className="btn btn-primary" onClick={handleSave}>
                            <span>Speichern und ausloggen</span>
                        </button>
                    </div>
                </Form>
            </div>
        </div>
    )
}

export default ManagePersonalData;