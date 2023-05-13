import {useEffect, useState} from "react";
import {deleteLesson, getMyLessons, searchLessons} from "../services/LessonService";
import {useLocation, useNavigate} from "react-router-dom";

const ShowLessons = () => {

    let navigate = useNavigate();

    const [lessons, setLessons] = useState([]);

    const location = useLocation();
    let query = location.state.query;

    useEffect(() => {
        searchLessons(query).then((res) => {
            console.log(res);
            setLessons(res.data);
        });
    }, []);

    const onShowLesson = (e) => {
        navigate("/show-lesson", {state: {id:e}})
    }


    return (
        <div>
            <h2 className="text-center"> Suchergebnis </h2>
            <div className="row">
                <table className="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Fach</th>
                        <th>Klasse</th>
                        <th>Aktionen</th>

                    </tr>
                    </thead>
                    <tbody>
                    {
                        lessons.map(
                            (les, index) =>
                                <tr key={index}>
                                    <td> {les.lessonId} </td>
                                    <td> {les.name}</td>
                                    <td> {les.subject}</td>
                                    <td> {les.grade}</td>
                                    <td>
                                        <button className="btn btn-primary" onClick={() => onShowLesson(les.lessonId)}>Ansehen</button>
                                    </td>
                                </tr>
                        )
                    }
                    </tbody>
                </table>

            </div>
        </div>
    );
}

export default ShowLessons;