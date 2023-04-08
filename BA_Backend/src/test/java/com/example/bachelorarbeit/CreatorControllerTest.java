package com.example.bachelorarbeit;

import com.example.bachelorarbeit.controllers.lesson.CreatorController;
import com.example.bachelorarbeit.models.user_management.User;
import com.example.bachelorarbeit.payload.request.ChangeUserRequest;
import com.example.bachelorarbeit.payload.response.MessageResponse;
import com.example.bachelorarbeit.repository.user_management.UserRepository;
import com.example.bachelorarbeit.security.jwt.JwtUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CreatorControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private CreatorController creatorController;

    @Test
    public void testGetUser() throws Exception {
        // Setup
        Long id = 1L;
        User user = new User();
        user.setUser_id(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Test
        User result = creatorController.getUser(id);

        // Verify
        Assert.assertEquals(user, result);
    }

    @Test(expected = Exception.class)
    public void testGetUserNotFound() throws Exception {
        // Setup
        Long id = 1L;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Test
        creatorController.getUser(id);
    }

    @Test
    public void testUpdateUser() throws Exception {
        String token = "token";
        Long userId = 1L;
        User user = new User();
        user.setUser_id(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn(user.getUsername());

        ChangeUserRequest request = new ChangeUserRequest();
        request.setUserId(userId);
        request.setUserName("new name");
        request.setEmail("new email");

        ResponseEntity<?> response = creatorController.updateUser(token, request);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("User saved successfully!", ((MessageResponse) response.getBody()).getMessage());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void testUpdateUserNotAuthorized() throws Exception {
        String token = "dummy token";
        Long userId = 1L;
        User user = new User();
        user.setUser_id(userId);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn(user.getUsername());

        ChangeUserRequest request = new ChangeUserRequest();
        request.setUserId(userId);
        request.setUserName("new name");
        request.setEmail("new email");

        ResponseEntity<?> response = creatorController.updateUser(token, request);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("User not saved!", ((MessageResponse) response.getBody()).getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test(expected = Exception.class)
    public void testUpdateUserNotFound() throws Exception {
        String token = "token";
        Long userId = 1L;
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("username");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ChangeUserRequest request = new ChangeUserRequest();
        request.setUserId(userId);
        request.setUserName("new name");
        request.setEmail("new email");

        // Test
        creatorController.updateUser(token, request);
    }
}
