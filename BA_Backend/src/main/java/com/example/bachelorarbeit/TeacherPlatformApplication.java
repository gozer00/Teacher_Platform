package com.example.bachelorarbeit;

import com.example.bachelorarbeit.fileUpload.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        FileStorageProperties.class
})
@SpringBootApplication
public class TeacherPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherPlatformApplication.class, args);
    }

}
