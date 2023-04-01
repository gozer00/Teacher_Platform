import axios from "axios";
import authHeader from "./auth-header";

const FILES_UPLOAD_BASE_URL = "http://localhost:8080/api/files";
const FILES_DOWNLOAD_BASE_URL = "http://localhost:8080/download-file";
const FILES_DELETE_BASE_URL = "http://localhost:8080/delete-file";



export const uploadFile = (file) => {
    let header = {
        "Authorization": 'Bearer '+JSON.parse(localStorage.getItem('user')).accessToken,
        "Content-type": 'multipart/form-data'
    }

    return axios.post(FILES_UPLOAD_BASE_URL+"/upload-file", file, {
        headers: header
    })
}

export const downloadFile = (fileName) => {
    return axios.get(fileName, {headers: authHeader(), responseType: "arraybuffer"})
}

export const deleteFile = (fileName) => {
    return axios.delete(FILES_DELETE_BASE_URL + fileName.substring(fileName.lastIndexOf("/"), fileName.length), {headers: authHeader()})
}
