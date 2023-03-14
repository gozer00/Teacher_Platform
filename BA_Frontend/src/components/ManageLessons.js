import {useEffect, useState} from "react";
import {deleteLesson, getMyLessons} from "../services/LessonService";
import {useNavigate} from "react-router-dom";

const ManageLessons = () => {

    let navigate = useNavigate();

    const [lessons, setLessons] = useState([]);

    const handleUpdate = (e) => {
        navigate("/update-lesson", {state: {id: e}});
    }

    const handleDelete = (e) => {
        deleteLesson(e).then(() => {
            setLessons(lessons.filter(lesson => lesson.lessonId !== e));
        });
    }

    const onShowLesson = (e) => {
        navigate("/show-lesson", {state: {id:e}})
    }

    useEffect(() => {
        getMyLessons().then((res) => {
            setLessons(res.data);
        });
    }, []);


    return (
        <div>
            <h2 className="text-center"> Meine Unterrichtseinheiten </h2>
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
                                    <td> {les.metaInformation.name}</td>
                                    <td> {les.metaInformation.subject}</td>
                                    <td> {les.metaInformation.grade}</td>
                                    <td>
                                        <button className="btn btn-primary" onClick={() => onShowLesson(les.lessonId)}>Ansehen</button>
                                        <button className="btn btn-primary" onClick={() => handleUpdate(les.lessonId)}>Ändern</button>
                                        <button className="btn btn-danger" onClick={() => handleDelete(les.lessonId)}>Löschen</button>
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

export default ManageLessons;