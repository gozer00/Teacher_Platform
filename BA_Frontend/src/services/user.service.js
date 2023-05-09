import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "http://localhost:8080/api/test/";
const CHANGE_API_URL = "http://localhost:8080/api/user/"

const getPublicContent = () => {
    return axios.get(API_URL + "all");
};

const getUserBoard = () => {
    return axios.get(API_URL + "user", { headers: authHeader() });
};

const getModeratorBoard = () => {
    return axios.get(API_URL + "mod", { headers: authHeader() });
};

const getAdminBoard = () => {
    return axios.get(API_URL + "admin", { headers: authHeader() });
};

export const updateUser = (updateUserRequest) => {
    return axios.put(CHANGE_API_URL + "update", updateUserRequest, {headers: authHeader()});
}

export const getUser = (id) => {
    return axios.get(CHANGE_API_URL +id, {headers: authHeader()});
}

export const deleteUser = (id) => {
    return axios.delete(CHANGE_API_URL+"delete/"+id, {headers: authHeader()});
}

export default {
    getPublicContent,
    getUserBoard,
    getModeratorBoard,
    getAdminBoard,
    updateUser,
    getUser
};
