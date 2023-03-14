import React, {Component, useRef} from 'react';
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";

class PhaseComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: 'default',
            minutes: 0,
            learningGoal: 'default',
            teacherAction: 'default',
            pupilAction: 'default',
            teachingForm: 'default',
            media: 'default',
            material: 'default'
        };

        this.changeNameHandler = this.changeNameHandler.bind(this);
        this.changeLengthHandler = this.changeLengthHandler.bind(this);
        this.changeLearningGoalsHandler = this.changeLearningGoalsHandler.bind(this);
        this.changeTeacherActionHandler = this.changeTeacherActionHandler.bind(this);
        this.changePupilActionHandler = this.changePupilActionHandler.bind(this);
        this.changeTeachingFormHandler = this.changeTeachingFormHandler.bind(this);
        this.changeMediaHandler = this.changeMediaHandler.bind(this);
        this.changeMaterialHandler = this.changeMaterialHandler.bind(this);
    }
    render() {

        return (
            <div className="card">
                <h5>Phase</h5>
                <Form>
                    <div className="form-group">
                        <label>Name der Phase</label>
                        <Input
                            type="text"
                            className="form-control"
                            onChange={this.changeNameHandler}
                        />
                    </div>

                    <div className="form-group">
                        <label>Dauer in Minuten</label>
                        <Input
                            type="text"
                            className="form-control"
                            onChange={this.changeLengthHandler}
                        />
                    </div>

                    <div className="form-group">
                        <label>Lernziele</label>
                        <Input
                            type="text"
                            className="form-control"
                            onChange={this.changeLearningGoalsHandler}
                        />
                    </div>

                    <div className="form-group">
                        <label>Lehrerhandeln</label>
                        <Input
                            type="text"
                            className="form-control"
                            onChange={this.changeTeacherActionHandler}
                        />
                    </div>

                    <div className="form-group">
                        <label>Schülerhandeln</label>
                        <Input
                            type="text"
                            className="form-control"
                            onChange={this.changePupilActionHandler}
                        />
                    </div>

                    <div className="form-group">
                        <label>Lehr-Lern-Situation</label>
                        <Input
                            type="text"
                            className="form-control"
                            onChange={this.changeTeachingFormHandler}
                        />
                    </div>

                    <div className="form-group">
                        <label>Medien</label>
                        <Input
                            type="text"
                            className="form-control"
                            onChange={this.changeMediaHandler}
                        />
                    </div>

                    <div className="form-group">
                        <label>Materialien</label>
                        <Input
                            type="text"
                            className="form-control"
                            onChange={this.changeMaterialHandler}
                        />
                    </div>
                </Form>
            </div>
        );
    }

    changeNameHandler=(event) => {
        //console.log('name of phase was changed to ' + this.state.name);
        this.setState({name: event.target.value});
    }

    changeLengthHandler=(event) => {
        this.setState({minutes: event.target.value});
    }

    changeLearningGoalsHandler=(event) => {
        this.setState({learningGoal: event.target.value});
    }

    changeTeacherActionHandler=(event) => {
        this.setState({teacherAction: event.target.value});
    }

    changePupilActionHandler=(event) => {
        this.setState({pupilAction: event.target.value});
    }

    changeTeachingFormHandler=(event) => {
        this.setState({teachingForm: event.target.value});
    }

    changeMediaHandler=(event) => {
        this.setState({media: event.target.value});
    }

    changeMaterialHandler=(event) => {
        this.setState({material: event.target.value});
    }
}

export default PhaseComponent;