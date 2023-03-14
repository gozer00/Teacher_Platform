import {useState} from "react";
import {uploadFile} from "../services/FileService";

const FileUploadTestComponent = () => {

    const [file, setFile] = useState();

    const handleFileChange=(e)=>{
        if(e.target.files) {
            setFile(e.target.files[0]);
        }
    }

    const handleUploadClick =()=>{
        if(!file) {
            return;
        }
        const formData = new FormData();
        formData.append('file', file);
        uploadFile(formData)
            .then((data) => console.log(data))
            .catch((err) => console.error(err));
    }

    const onOkay = () => {

    }

    return(
        <div>
            <input type="file" onChange={handleFileChange} />

            <div>{file && `${file.name} - ${file.type}`}</div>

            <button onClick={handleUploadClick}>Upload</button>
        </div>
    );
}

export default FileUploadTestComponent;