package com.example.bachelorarbeit.controllers.files;

import com.example.bachelorarbeit.payload.response.MessageResponse;
import com.example.bachelorarbeit.payload.response.UploadFileResponse;
import com.example.bachelorarbeit.fileUpload.FileStorageService;
import com.example.bachelorarbeit.repository.lesson.FileURIRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Endpoint for uploading files.
     * @param file the uploaded file
     * @return UploadFileResponse containting the URI for download
     */
    @PostMapping(value="/api/files/upload-file", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = "http://localhost:8080/download-file/" + fileName;

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    /**
     * Endpoint for downloading files.
     * @param fileName name of the file
     * @param request request
     * @return ResponseEntity containing the file
     * @throws IOException if file wasn't found
     */
    @GetMapping("/download-file/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * Endpoint for deleting files.
     * @param name file name
     * @return ResponseEntity containing success or error message
     */
    @DeleteMapping("/delete-file/{name:.+}")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@PathVariable String name) {
        String message = "";
        // Load file name
        String fileuri = "http://localhost:8080/download-file/" + name;
        try {
            // Try to delete file
            boolean existed = fileStorageService.delete(fileuri);

            if (existed) {
                // Send success message
                message = "Delete the file successfully: " + fileuri;
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            }

            // Send failure message
            message = "The file does not exist!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(message));
        } catch (Exception e) {
            e.printStackTrace();
            message = "Could not delete the file: " + fileuri + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(message));
        }
    }
}
