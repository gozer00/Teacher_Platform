package com.example.bachelorarbeit.controllers.lesson;

import com.example.bachelorarbeit.models.lesson.Lesson;
import com.example.bachelorarbeit.models.lesson.MetaInformation;
import com.example.bachelorarbeit.models.user_management.User;
import com.example.bachelorarbeit.payload.request.ChangeUserRequest;
import com.example.bachelorarbeit.payload.request.SaveLessonRequest;
import com.example.bachelorarbeit.payload.response.MessageResponse;
import com.example.bachelorarbeit.repository.lesson.LessonRepository;
import com.example.bachelorarbeit.repository.user_management.UserRepository;
import com.example.bachelorarbeit.security.jwt.AuthTokenFilter;
import com.example.bachelorarbeit.security.jwt.JwtUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class CreatorController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Endpoint for getting user.
     * @param id id of the user
     * @return User object
     * @throws Exception if user doesn't exist
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(()->new Exception("User doesn't exist"));
    }

    /**
     * Endpoint for updating user details.
     * @param token JWT
     * @param request ChangeUserRequest
     * @return ResponseEntity containing success or error message
     * @throws Exception if user with id doesn't exist
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader (name="Authorization") String token,
                                          @RequestBody ChangeUserRequest request) throws Exception {
        if (Objects.equals(request.getUserId(), jwtUtils.getUserFromToken(token).getUser_id())) { // Check authority
            // Load User from database
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            // Write new values
            user.setUsername(request.getUserName());
            user.setEmail(request.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User saved successfully!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User not saved!"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@RequestHeader (name="Authorization") String token,
                                        @PathVariable Long id) throws Exception {
        if (Objects.equals(id, jwtUtils.getUserFromToken(token).getUser_id())) { // Check authority
            // Load User from database
            userRepository.deleteByUser_id(id);
            lessonRepository.deleteAllByCreator_User_id(id);
            return ResponseEntity.ok(new MessageResponse("User and lessons deleted successfully!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User not deleted!"));
        }
    }
}
