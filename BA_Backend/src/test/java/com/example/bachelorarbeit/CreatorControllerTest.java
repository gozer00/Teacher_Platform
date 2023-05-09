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

    /**
     * Test method for getting user from database
     * @throws Exception
     */
    @Test
    public void testGetUser() throws Exception {
        // Setup
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Test
        User result = creatorController.getUser(id);

        // Verify
        Assert.assertEquals(user, result);
    }

    /**
     * Test method with user not found
     * @throws Exception
     */
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
        // start values
        String token = "token";
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn(user.getUsername());

        ChangeUserRequest request = new ChangeUserRequest();
        request.setUserId(userId);
        request.setUserName("new name");
        request.setEmail("new email");

        // call updateUser function
        ResponseEntity<?> response = creatorController.updateUser(token, request);

        // verification
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("User saved successfully!", ((MessageResponse) response.getBody()).getMessage());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    /**
     * Test method testing a ChangeUserRequest with not authorized user
     * @throws Exception
     */
    @Test
    public void testUpdateUserNotAuthorized() throws Exception {
        // start values
        String token = "dummy token";
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Mockito.when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn(user.getUsername());

        ChangeUserRequest request = new ChangeUserRequest();
        request.setUserId(userId);
        request.setUserName("new name");
        request.setEmail("new email");

        // load response
        ResponseEntity<?> response = creatorController.updateUser(token, request);

        // verification
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("User not saved!", ((MessageResponse) response.getBody()).getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    /**
     * Test method for updating lessons. Test case: wrong user
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdateUserNotFound() throws Exception {
        // setup
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
