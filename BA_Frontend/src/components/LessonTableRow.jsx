import React, {useState} from "react";


const LessonTableRow = (lesson) => {
    const [expanded, setExpanded] = useState(false);

    const toggleExpander = (e) => {
        if (!expanded) {
            setExpanded(true);
        } else {
            setExpanded(false);
        }
    }

    return(
        <tr key="main" onClick={toggleExpander}>
            <td><input className="uk-checkbox" type="checkbox" /></td>
            <td className="uk-text-nowrap">{lesson.lessonId}</td>
        </tr>,
        this.state.expanded && (
            <tr className="expandable" key="tr-expander">
                <td className="uk-background-muted" colSpan={6}>
                    <div ref="expanderBody" className="inner uk-grid">

                    </div>
                </td>
            </tr>
    ));
}