package com.example.bachelorarbeit.repository.lesson;

import com.example.bachelorarbeit.models.lesson.FileURI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileURIRepository extends JpaRepository<FileURI, Long> {
    //List<FileURI> findFileURIByUri(String uri);

    FileURI findFileURIByUri(String uri);

    boolean existsFileURIByUri(String uri);
}
