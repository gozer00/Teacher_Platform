package com.example.bachelorarbeit.controllers.lesson;

import com.example.bachelorarbeit.models.lesson.Lesson;
import com.example.bachelorarbeit.models.lesson.MetaInformation;
import com.example.bachelorarbeit.models.user_management.User;
import com.example.bachelorarbeit.payload.request.ChangeUserRequest;
import com.example.bachelorarbeit.payload.request.SaveLessonRequest;
import com.example.bachelorarbeit.payload.response.MessageResponse;
import com.example.bachelorarbeit.repository.user_management.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class CreatorController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/my")
    public User getLesson(@PathVariable Long id) throws Exception {
        //todo: check user authority
        return userRepository.findById(id)
                .orElseThrow(()->new Exception("User doesn't exist"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLesson(@RequestHeader (name="Authorization") String token,
                                          @RequestBody ChangeUserRequest request) throws Exception {
        //todo: check user authority
        System.out.println(new Gson().toJson(request));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new Exception("User not found"));
        user.setUsername(request.getUserName());
        user.setEmail(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("User saved successfully!"));
    }
}
