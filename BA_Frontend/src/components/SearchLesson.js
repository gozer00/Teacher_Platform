import {useNavigate} from "react-router-dom";
import React, {useState} from "react";

const Home = () => {
    let navigate = useNavigate();

    const [input, setInput] = useState('');

    const onChange = (e) => {
        const input = e.target.value;
        setInput(input);
    }

    const onClick = () => {
        console.log(input);
        navigate('/show-lessons', {state: {query: input}});
    }

    return (
        <div className="container">
            <header className="jumbotron">
                <h3>Suchen</h3>
                <input
                    name='media'
                    type="text"
                    className="form-control"
                    onChange={event => onChange(event)}
                />
                <button className="btn btn-primary" onClick={onClick}> Suchen </button>
            </header>
        </div>
    );
};

export default Home;
