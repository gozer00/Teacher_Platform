import React, {useCallback, useEffect} from 'react';
import {useLocation, useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {clearMessage} from "../actions/message";
import {logout} from "../actions/auth";

const HeaderComponent = () => {
    let navigate = useNavigate();

    const { user: currentUser } = useSelector((state) => state.auth);
    const dispatch = useDispatch();

    let location = useLocation();

    const onHome=() => {
        navigate("/home")
    }

    const onProfile=() => {
        navigate("/profile")
    }

    const onSearch=() => {
        navigate("/search")
    }

    const onLogin=() => {
        navigate("/login")
    }

    const onRegister=() => {
        navigate("/register")
    }

    useEffect(() => {
        if (["/login", "/register"].includes(location.pathname)) {
            dispatch(clearMessage()); // clear message when changing location
        }
    }, [dispatch, location]);

    const logOut = useCallback(() => {
        dispatch(logout());
        navigate("/home")
    }, [dispatch]);

    useEffect(() => {
        if (currentUser) {
            setShowAdminBoard(currentUser.roles.includes("ROLE_ADMIN"));
        } else {
            setShowAdminBoard(false);
        }
    }, [currentUser]);

    return (
        <div>
            <nav className="navbar navbar-expand navbar-dark bg-dark">
                <div className="navbar-nav mr-auto">
                    <button className="btn btn-primary nb" onClick={onHome}>
                        Start
                    </button>
                </div>

                <div className="navbar-nav mr-auto">
                    <button className="nav-item btn btn-primary nb" onClick={onSearch}>
                        Unterricht suchen
                    </button>
                </div>

                {currentUser ? (
                    <div className="navbar-nav ml-auto">
                        <button className="nav-item btn btn-primary nb" onClick={onProfile}>
                            Mein Bereich: {currentUser.username}
                        </button>

                        <button className="nav-item btn btn-primary nb" onClick={logOut}>
                            Ausloggen
                        </button>
                    </div>
                ) : (
                    <div className="navbar-nav ml-auto">
                        <button className="nav-item btn btn-primary nb" onClick={onLogin}>
                            Einloggen
                        </button>

                        <button className="nav-item btn btn-primary nb" onClick={onRegister}>
                            Registrieren
                        </button>
                    </div>
                )}
            </nav>
        </div>
    );
}

export default HeaderComponent;