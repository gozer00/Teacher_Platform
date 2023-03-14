import React, {useEffect, useRef, useState} from "react";
import {Link, Navigate, useLocation, useNavigate} from 'react-router-dom';
import {useDispatch, useSelector} from "react-redux";
import Input from "react-validation/build/input";
import Form from "react-validation/build/form";
import {createLesson, getLesson, updateLesson} from "../services/LessonService";
import {deleteFile, uploadFile} from "../services/FileService";
import equals from "validator/es/lib/equals";

const UpdateLesson = () => {
    const { user: currentUser } = useSelector((state) => state.auth);

    let navigate = useNavigate();

    const location = useLocation();
    let requestId = location.state.id;

    const form = useRef();

    const [title, setTitle] = useState("Neue Unterrichtseinheit erstellen");
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
    const [isPublic, setIsPublic] = useState(false);
    const [lessonId, setLessonId] = useState(-1)

    const [phaseFields, setPhaseFields] = useState([]);
    const [phaseFiles, setPhaseFiles] = useState([]);

    useEffect(() => {
        if(requestId !== -1) {
            setTitle("Unterrichtseinheit ändern")
            getLesson(requestId).then((res) => {
                console.log(res.data.metaInformation);
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
                setIsPublic(res.data.metaInformation.public);
                setPhaseFields(res.data.procedurePlan);
                setLessonId(res.data.lessonId);
            });
        }
    }, []);

    const onChangeName = (e) => {
        const name = e.target.value;
        setName(name);
    }

    const onChangeSubject = (e) => {
        const subject = e.target.value;
        setSubject(subject);
    }

    const onChangeGrade = (e) => {
        const grade = e.target.value;
        setGrade(grade);
    }

    const onChangeSchool = (e) => {
        const school = e.target.value;
        setSchool(school);
    }

    const onChangeState = (e) => {
        const state = e.target.value;
        setState(state);
    }

    const onChangeLessonThema = (e) => {
        const lessonThema = e.target.value;
        setLessonThema(lessonThema);
    }

    const onChangeMedia = (e) => {
        const media = e.target.value;
        setMedia(media);
    }

    const onChangeLessonType = (e) => {
        const lessonType = e.target.value;
        setLessonType(lessonType);
    }

    const onChangeLearningGoals = (e) => {
        const learningGoals = e.target.value;
        setLearningGoals(learningGoals);
    }

    const onChangePreKnowledge = (e) => {
        const pre = e.target.value;
        setPreKnowledge(pre);
    }

    const onChangeResources = (e) => {
        const res = e.target.value;
        setResources(res);
    }

    const onChangeKeywords = (e) => {
        const key = e.target.value;
        setKeywords(key);
    }

    const onChangeIsPublic = (e) => {
        setIsPublic(!isPublic);
    }

    const onAddPhaseClick = (e) => {
        e.preventDefault(); //prevents reloading of the page
        let newPhase = {
            name:'',
            minutes:0,
            learningGoal:'',
            teacherAction: '',
            pupilAction: '',
            teachingForm: '',
            media: '',
            fileURIs: []
        };
        setPhaseFields([...phaseFields, newPhase]);
    };

    const handleDeletePhase = (index) => {
        let data = phaseFields;
        if(data.length === 1) {
            data = [];
        } else {
            data.splice(index, 1);
        }
        setPhaseFields([...data]);
    }

    const handlePhaseChange = (index, event) => {
        if (event.target.name === "files") {
            console.log(event.target.files);
            let temp = phaseFiles
            temp = temp.filter(f => f.phase !== index)
            Array.from(event.target.files).forEach((file) => {
                temp = [...temp, {"phase": index, "file":file}]
                //console.log(file);
                //setPhaseFiles([...phaseFiles, {"phase": index, "file":file}]);
            })
            setPhaseFiles(temp);
            //console.log(data[index][event.target.name])
        } else {
            let data = [...phaseFields];
            data[index][event.target.name] = event.target.value;
            setPhaseFields(data);
        }
        //console.log("new value: " + data[index][event.target.name])
        console.log(phaseFiles)
    }

    const handleDeleteFile = (fileName, index) => {
        deleteFile(fileName).then(() => {
            let data = phaseFields;
            console.log(data[index].fileURIs);
            data[index].fileURIs = data[index].fileURIs.filter(f => f.uri !== fileName);
            console.log(data[index].fileURIs);
            setPhaseFields([...data]);
        });
    }

    const discard = () => {
        if (requestId !== -1) {
            navigate("/manage-lessons");
        } else {
            navigate("/profile");
        }
    }

    const handleUp = (index) => {
        if(index === 0) {
            return;
        }
        let data = phaseFields;
        let tmp = data[index-1];
        data[index-1] = data[index];
        data[index] = tmp;
        setPhaseFields([...data]);
    }

    const handleDown = (index) => {
        if(index === phaseFields.length-1) {
            return;
        }
        let data = phaseFields;
        let tmp = data[index+1];
        data[index+1] = data[index];
        data[index] = tmp;
        setPhaseFields([...data]);
    }

    const saveLesson = async (e) => {
        e.preventDefault();
        let tempURIs = [];
        /*let counter = 0;
        phaseFields.forEach(phase => {
            phase.fileURIs.forEach(fileUri => {
                tempURIs = [...tempURIs, {"uri":fileUri.uri, "phase":counter}];
            });
            counter++;
        });*/
        for (const fileTuple of phaseFiles) {
            const formData = new FormData();
            console.log(fileTuple)
            formData.append("file", fileTuple.file)
            await uploadFile(formData)
                // eslint-disable-next-line no-loop-func
                .then(function (response) {
                    console.log(response.data.fileDownloadUri);
                    tempURIs = [...tempURIs, {
                        "phase": fileTuple.phase,
                        "uri": response.data.fileDownloadUri
                    }];
                    console.log(tempURIs)
                })
        }
        //console.log(lesson)
        let lesson = {
            "name": name,
            "subject": subject,
            "grade": grade,
            "school": school,
            "state": state,
            "lessonThema": lessonThema,
            "media": media,
            "lessonType": lessonType,
            "learningGoals": learningGoals,
            "preKnowledge": preKnowledge,
            "resources": resources,
            "keywords": keywords,
            "pPublic": isPublic,
            "procedurePlan": phaseFields,
            "fileURIs": tempURIs,
            "userId": currentUser.id,
            "lessonId": lessonId
        };
        if (requestId !== -1) {
            updateLesson(lesson);
            navigate("/manage-lessons");
        } else {
            createLesson(lesson);
            navigate("/profile");
        }

    }

    if (!currentUser) {
        return <Navigate to="/login" />;
    }

    return (
        <div className="container">
            <header className="jumbotron">
                <h1>
                    {title}
                </h1>
            </header>
            <Form ref={form}>
                <div className="form-group">
                    <label htmlFor="name">Name der Einheit</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="name"
                        value={name}
                        onChange={onChangeName}
                        validations={[required]}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="subject">Unterrichtsfach</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="subject"
                        value={subject}
                        onChange={onChangeSubject}
                        validations={[required]}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="grade">Klassenstufe</label>
                    <Input
                        type="number"
                        className="form-control"
                        name="grade"
                        value={grade}
                        onChange={onChangeGrade}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="school">Schulart</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="school"
                        value={school}
                        onChange={onChangeSchool}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="state">Bundesland</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="state"
                        value={state}
                        onChange={onChangeState}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="lessonThema">Thema</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="lessonThema"
                        value={lessonThema}
                        onChange={onChangeLessonThema}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="media">Medien</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="media"
                        value={media}
                        onChange={onChangeMedia}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="lessonType">Art der Stunde</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="lessonType"
                        value={lessonType}
                        onChange={onChangeLessonType}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="learningGoals">Lernziele</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="learningGoals"
                        value={learningGoals}
                        onChange={onChangeLearningGoals}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="preKnowledge">Benötigtes Vorwissen</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="preKnowledge"
                        value={preKnowledge}
                        onChange={onChangePreKnowledge}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="resources">Weiterführende Quellen</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="resources"
                        value={resources}
                        onChange={onChangeResources}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="keywords">Schlagwörter für die Suche</label>
                    <Input
                        type="text"
                        className="form-control"
                        name="keywords"
                        value={keywords}
                        onChange={onChangeKeywords}
                    />
                </div>
            </Form>

            <hr/>
            <h3>Unterrichtsablaufplan</h3>
            {phaseFields.map((input, index) => {
                return (
                    <div className="form-group" key={index}>
                        <h5>Phase {index+1}</h5>
                        <label>Name der Phase</label>
                        <input
                            name='name'
                            type="text"
                            className="form-control"
                            value={phaseFields[index].name}
                            onChange={event => handlePhaseChange(index, event)}
                        />
                        <label>Dauer in Minuten</label>
                        <input
                            name='minutes'
                            type="number"
                            className="form-control"
                            value={phaseFields[index].minutes}
                            onChange={event => handlePhaseChange(index, event)}
                        />
                        <label>Lernziele</label>
                        <input
                            name='learningGoal'
                            type="text"
                            className="form-control"
                            value={phaseFields[index].learningGoal}
                            onChange={event => handlePhaseChange(index, event)}
                        />
                        <label>Lehrerhandeln</label>
                        <input
                            name='teacherAction'
                            type="text"
                            className="form-control"
                            value={phaseFields[index].teacherAction}
                            onChange={event => handlePhaseChange(index, event)}
                        />
                        <label>Schülerhandeln</label>
                        <input
                            name='pupilAction'
                            type="text"
                            className="form-control"
                            value={phaseFields[index].pupilAction}
                            onChange={event => handlePhaseChange(index, event)}
                        />
                        <label>Lehr-Lern-Situation</label>
                        <input
                            name='teachingForm'
                            type="text"
                            className="form-control"
                            value={phaseFields[index].teachingForm}
                            onChange={event => handlePhaseChange(index, event)}
                        />
                        <label>Medien</label>
                        <input
                            name='media'
                            type="text"
                            className="form-control"
                            value={phaseFields[index].media}
                            onChange={event => handlePhaseChange(index, event)}
                        />
                        <label>Materialien</label>
                        <table>
                            <tbody>
                                {input.fileURIs.map((l, idx) =>
                                    <tr key = {idx}>
                                        <td>{l.uri}</td>
                                        <td><button className="btn btn-danger" onClick={() => handleDeleteFile(l.uri, index)}> Datei löschen </button></td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                        <input
                            name='files'
                            type="file"
                            multiple
                            className="form-control"
                            onChange={event => handlePhaseChange(index, event)}
                        />
                        <label>Aktionen</label>
                        <div className="form-group" style={{marginTop: "10px"}}>
                            <button className="btn btn-primary" onClick={() => handleUp(index)}>Nach oben</button>
                            <button className="btn btn-primary" style={{marginLeft: "10px"}} onClick={() => handleDown(index)}>Nach unten</button>
                            <button className="btn btn-danger" style={{marginLeft: "10px"}} onClick={() => handleDeletePhase(index)}>Löschen</button>
                        </div>
                        <hr />
                    </div>
                )
            })}

            <button className="btn btn-light" onClick={onAddPhaseClick}>Neue Phase hinzufügen</button>
            <hr/>

            <Form>
                <div className="form-group">
                    <label htmlFor="isPublic">Öffentlich sichtbar</label>
                    <Input
                        type="checkbox"
                        name="isPublic"
                        checked={isPublic}
                        onChange={onChangeIsPublic}
                    />
                </div>

                <div className="form-group" style={{marginTop: "10px"}}>
                    <button className="btn btn-primary btn-block" onClick={saveLesson}>
                        <span>Speichern</span>
                    </button>

                    <button className="btn btn-danger btn-block" onClick={discard} style={{marginLeft: "10px"}}>
                        Abbrechen
                    </button>
                </div>
            </Form>
        </div>
    );
};

const required = (value) => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

export default UpdateLesson;