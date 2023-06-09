import './App.css';
import {Route, Routes} from 'react-router-dom';
import HeaderComponent from "./components/HeaderComponent";
import FooterComponent from "./components/FooterComponent";
import Home from "./components/Home";
import Login from "./components/Login";
import Register from "./components/Register";
import PersonalArea from "./components/PersonalArea";
import SearchLesson from "./components/SearchLesson";
import ShowLessons from "./components/ShowLessons";
import ManageLessons from "./components/ManageLessons";
import UpdateLesson from "./components/UpdateLesson";
import ShowLesson from "./components/ShowLesson";
import ManagePersonalData from "./components/ManagePersonalData";

function App() {
    return (
        <div>
            <HeaderComponent/>
            <div className="container mt-3">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/profile" element={<PersonalArea />} />
                    <Route path="/create-lesson" element={<UpdateLesson />} />
                    <Route path="/search" element={<SearchLesson />} />
                    <Route path="/show-lessons" element={<ShowLessons/>} />
                    <Route path="/manage-lessons" element={<ManageLessons/>} />
                    <Route path="/update-lesson" element={<UpdateLesson/>} />
                    <Route path="/show-lesson" element={<ShowLesson/>} />
                    <Route path="/manage-personal-data" element={<ManagePersonalData/>}/>
                </Routes>
            </div>
            <FooterComponent/>
        </div>
    );
}

export default App;



