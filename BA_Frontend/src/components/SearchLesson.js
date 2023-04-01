import {useNavigate} from "react-router-dom";
import React, {useState} from "react";
import Form from "react-validation/build/form";

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
                <Form onSubmit={onClick}>
                    <input
                        name='media'
                        type="text"
                        className="form-control"
                        onChange={event => onChange(event)}
                    />
                    <button className="btn btn-primary" onClick={onClick}> Suchen </button>
                </Form>
            </header>
        </div>
    );
};

export default Home;
