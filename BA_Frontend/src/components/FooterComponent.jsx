import React, {Component} from 'react';

class FooterComponent extends Component {
    constructor(props){
        super(props)

        this.state = {

        }
    }
    render() {
        return (
            <div>
                <footer className="text-center text-lg-start bg-light text-muted">
                    <div className="text-center p-4">
                        Bachelorarbeit von Erich Gozebina: Web-Plattform zum Erstellen und Teilen von Unterrichtseinheiten
                    </div>
                </footer>
            </div>
        );
    }
}

export default FooterComponent;