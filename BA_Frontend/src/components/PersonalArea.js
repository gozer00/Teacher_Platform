import React from "react";
import {Navigate, useNavigate} from 'react-router-dom';
import { useSelector } from "react-redux";

const PersonalArea = () => {
    let navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);

    if (!currentUser) {
        return <Navigate to="/login" />;
    }

    const handleCreateLesson = () => {
        navigate("/create-lesson", {state: {id: -1}})
    }

    const handleManageLessons = () => {
        navigate("/manage-lessons")
    }

    const handleManagePersonal = () => {
        navigate("/manage-personal-data")
    }


    return (
        <div className="container">
            <header className="jumbotron">
                <h1>
                    Mein Bereich <strong>{currentUser.username}</strong>
                </h1>
            </header>
            <ul className="list-group">
                <li className="list-group-item">
                    <button className="btn btn-light" onClick={handleCreateLesson}> Neue Unterrichtseinheit erstellen </button>
                </li>
                <li className="list-group-item">
                    <button className="btn btn-light" onClick={handleManageLessons}> Meine Unterrichtseinheiten verwalten </button>
                </li>
                <li className="list-group-item">
                    <button className="btn btn-light" onClick={handleManagePersonal}> Meine persönlichen Daten verwalten </button>
                </li>
            </ul>
        </div>
    );
};

export default PersonalArea;
