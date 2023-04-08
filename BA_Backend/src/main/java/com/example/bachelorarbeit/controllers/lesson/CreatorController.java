package com.example.bachelorarbeit.controllers.lesson;

import com.example.bachelorarbeit.models.lesson.Lesson;
import com.example.bachelorarbeit.models.lesson.MetaInformation;
import com.example.bachelorarbeit.models.user_management.User;
import com.example.bachelorarbeit.payload.request.ChangeUserRequest;
import com.example.bachelorarbeit.payload.request.SaveLessonRequest;
import com.example.bachelorarbeit.payload.response.MessageResponse;
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
    private JwtUtils jwtUtils;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(()->new Exception("User doesn't exist"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader (name="Authorization") String token,
                                          @RequestBody ChangeUserRequest request) throws Exception {
        if (Objects.equals(request.getUserId(), getUserFromToken(token).getUser_id())) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            user.setUsername(request.getUserName());
            user.setEmail(request.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User saved successfully!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User not saved!"));
        }
    }

    /**
     * This method extracts the user from a JWT. When user doesn't exist, it throws UsernameNotFound exception.
     * @param token JWT as String (usually retrieved from HTTP header)
     * @return User object when exists in the database
     */
    private User getUserFromToken(String token) {
        String jwt = AuthTokenFilter.parseJwt(token);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with name: " + username));
    }
}
