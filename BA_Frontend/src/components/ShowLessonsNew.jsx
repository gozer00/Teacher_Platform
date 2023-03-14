import React from 'react';
import { render } from 'react-dom';
import {getLesson, searchLessons} from "../services/LessonService";



class ShowLessonsNew extends React.Component {
    state = { lessons: null }

    componentDidMount() {
        searchLessons()
            .then(response => response.json())
            .then(data => { this.setState({lessons: data.results}) });
    }

    render() {
        const { lessons } = this.state;
        const isLoading = lessons === null;
        return (
            <main>
                <div className="table-container">
                    <div className="uk-overflow-auto">
                        <table className="uk-table uk-table-hover uk-table-middle uk-table-divider">
                            <thead>
                            <tr>
                                <th className="uk-table-shrink" />
                                <th className="uk-table-shrink" />
                                <th className="uk-table-shrink">Avatar</th>
                                <th>Name</th>
                                <th>Fach</th>
                                <th>Klasse</th>
                            </tr>
                            </thead>
                            <tbody>
                            {isLoading
                                ? <tr><td colSpan={6} className="uk-text-center"><em className="uk-text-muted">Loading...</em></td></tr>
                                : lessons.map((lesson, index) =>
                                    <LessonTableRow lesson={lesson}/>
                                )
                            }
                            </tbody>
                        </table>
                    </div>
                </div>
            </main>
        );
    }
}

render(<ShowLessonsNew />, document.getElementById('root'));