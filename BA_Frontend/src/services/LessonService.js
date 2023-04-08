import axios from "axios";
import authHeader from "./auth-header";

const TEACHER_PLATFORM_API_BASE_URL = "http://localhost:8080/api/lessons";

export const getLesson=(id) => {
    return axios.get(TEACHER_PLATFORM_API_BASE_URL+ "/" + id, { headers: authHeader() });
}

export const searchLessons = (query) => {
    return axios.get(TEACHER_PLATFORM_API_BASE_URL+"/search?query="+query);
}

export const getMyLessons=() => {
    return axios.get(TEACHER_PLATFORM_API_BASE_URL+"/my", { headers: authHeader() });
}

export const createLesson=(lesson) => {
    console.log(lesson)
    console.log(authHeader())
    return axios.post(TEACHER_PLATFORM_API_BASE_URL+"/create", lesson, { headers: authHeader() });
}

export const updateLesson=(lesson) => {
    return axios.put(TEACHER_PLATFORM_API_BASE_URL+"/update", lesson, { headers: authHeader() });
}

export const deleteLesson=(lessonId) => {
    return axios.delete(TEACHER_PLATFORM_API_BASE_URL+"/delete/" + lessonId, { headers: authHeader() })
}
