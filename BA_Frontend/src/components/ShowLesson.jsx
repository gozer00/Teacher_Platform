import React, {useEffect, useRef, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {getLesson} from "../services/LessonService";
import {downloadFile} from "../services/FileService";
import {Button} from "react-bootstrap";

const ShowLesson = () => {
    const navigate = useNavigate();
    const location = useLocation();
    let requestId = location.state.id;

    const [name, setName] = useState("");
    const [subject, setSubject] = useState("");
    const [grade, setGrade] = useState(0);
    const [school, setSchool] = useState("");
    const [state, setState] = useState("");
    const [lessonThema, setLessonThema] = useState("");
    const [media, setMedia] = useState("");
    const [lessonType, setLessonType] = useState("");
    const [learningGoals, setLearningGoals] = useState("");
    const [preKnowledge, setPreKnowledge] = useState("");
    const [resources, setResources] = useState("");
    const [keywords, setKeywords] = useState("");

    const [phaseFields, setPhaseFields] = useState([]);

    const handleDownloadLink = (link) => {
        downloadFile(link).then((response) => {
            console.log(response)
            const fileURL = window.URL.createObjectURL(new Blob([new Uint8Array(response.data)]));
            let alink = document.createElement('a');
            alink.href = fileURL;
            let responseURL = response.request.responseURL;
            alink.download = responseURL.substring(responseURL.lastIndexOf("/") + 1, responseURL.length);
            alink.click();
        });
    }

    useEffect(() => {
        getLesson(requestId).then((res) => {
            setName(res.data.metaInformation.name);
            setSubject(res.data.metaInformation.subject);
            setGrade(res.data.metaInformation.grade);
            setSchool(res.data.metaInformation.school);
            setState(res.data.metaInformation.state);
            setLessonThema(res.data.metaInformation.lessonThema);
            setMedia(res.data.metaInformation.media);
            setLessonType(res.data.metaInformation.lessonType);
            setLearningGoals(res.data.metaInformation.learningGoals);
            setPreKnowledge(res.data.metaInformation.preKnowledge);
            setResources(res.data.metaInformation.resources);
            setKeywords(res.data.metaInformation.keywords);
            setIsPublic(res.data.metaInformation.isPublic);
            setPhaseFields(res.data.procedurePlan);
            setLessonId(res.data.lessonId);
        });
    }, []);

    return(
        <div>
            <h2 className="text-center"> {name} </h2>
            <div>
                <table className="table table-bordered">
                    <tbody>
                        <tr>
                            <td> Fach</td>
                            <td> {subject}</td>
                        </tr>
                        <tr>
                            <td> Klassenstufe </td>
                            <td> {grade}</td>
                        </tr>
                        <tr>
                            <td> Schulart</td>
                            <td> {school}</td>
                        </tr>
                        <tr>
                            <td> Bundesland</td>
                            <td> {state}</td>
                        </tr>
                        <tr>
                            <td> Thema</td>
                            <td> {lessonThema}</td>
                        </tr>
                        <tr>
                            <td> Benötigte Medien</td>
                            <td> {media}</td>
                        </tr>
                        <tr>
                            <td> Unterrichtsform</td>
                            <td> {lessonType}</td>
                        </tr>
                        <tr>
                            <td> Lernziele</td>
                            <td> {learningGoals}</td>
                        </tr>
                        <tr>
                            <td> Benötigtes Vorwissen</td>
                            <td> {preKnowledge}</td>
                        </tr>
                        <tr>
                            <td> Übergreifende Quellen</td>
                            <td> {resources}</td>
                        </tr>
                        <tr>
                            <td> Schlagwörter</td>
                            <td> {keywords}</td>
                        </tr>
                    </tbody>
                </table>
                <h3 className="text-center">Unterrichtsablaufplan</h3>
                <table className="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th>Phase</th>
                            <th>Dauer</th>
                            <th>Lernziele</th>
                            <th>Lehrerhandeln</th>
                            <th>Schülerhandeln</th>
                            <th>Lehr-Lern-Situation</th>
                            <th>Medien</th>
                            <th>Materialien</th>
                        </tr>
                    </thead>
                    <tbody>
                    {phaseFields.map((phase, index1) =>
                        <tr key={index1}>
                            <td>{phase.name}</td>
                            <td>{phase.minutes}</td>
                            <td>{phase.learningGoal}</td>
                            <td>{phase.teacherAction}</td>
                            <td>{phase.pupilAction}</td>
                            <td>{phase.teachingForm}</td>
                            <td>{phase.media}</td>
                            <td>
                                <table>
                                    <tbody>
                                        {phase.fileURIs.map((l, index2) =>
                                            <tr key={index2}><td><button className="btn btn-light" onClick={() => handleDownloadLink(l.uri, l.uri.length)}>{l.uri.substring(l.uri.lastIndexOf("/")+1)}</button></td></tr>
                                        )}
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
            <Button className="btn btn-danger" onClick={()=> navigate(-1)}>Zurück</Button>
        </div>
    );
}

export default ShowLesson;