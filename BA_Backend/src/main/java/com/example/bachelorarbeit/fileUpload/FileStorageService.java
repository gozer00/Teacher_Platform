package com.example.bachelorarbeit.fileUpload;

import com.example.bachelorarbeit.models.lesson.FileURI;
import com.example.bachelorarbeit.repository.lesson.FileURIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;


    @Autowired
    private FileURIRepository fileURIRepository;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Service method that stores a file into filesystem of the server
     * @param file MultipartFile
     * @return String filename
     */
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Service method that loads the file from the filesystem
     * @param fileName name of the requested file
     * @return Resource object
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            // load resource
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new ByteArrayResource(Files.readAllBytes(filePath));
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Service method that deletes the file.
     * @param filename name of the file to delete
     * @return Boolean success
     */
    public boolean delete(String filename) {
        try {
            Path file = fileStorageLocation.resolve(filename.substring(filename.lastIndexOf("/")+1));
            System.out.println(file);
            // delete from database
            FileURI uri = fileURIRepository.findFileURIByUri(filename);
            fileURIRepository.delete(uri);

            return Files.deleteIfExists(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
