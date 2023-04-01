package com.example.bachelorarbeit;
/*
import com.example.bachelorarbeit.controllers.user_management.AuthController;
import com.example.bachelorarbeit.payload.request.LoginRequest;
import com.example.bachelorarbeit.payload.response.JwtResponse;
import com.example.bachelorarbeit.security.jwt.JwtUtils;
import com.example.bachelorarbeit.security.services.UserDetailsImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.catalina.Authenticator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@SpringBootTest
@AutoConfigureMockMvc
public class WithMockUserTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthController controller;

    @Autowired
    AuthenticationConfiguration authConfiguration;

    @Test
    public void testSearchWithoutAuth() throws Exception {
        this.mockMvc.perform(
                formLogin("http://localhost:8080/api/auth/signin")
                        .user("test_user").password("123456"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void testAuthenticateUser_success() throws Exception {
        // create a mock LoginRequest with valid credentials
        LoginRequest validRequest = new LoginRequest("testuser", "testpassword");

        // mock the authentication manager to return a valid authentication object
        Authentication authMock = Mockito.mock(Authentication.class);
        AuthenticationManager authenticationManager = authConfiguration.getAuthenticationManager();
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);

        // mock the UserDetailsImpl object to return a valid set of user details
        UserDetailsImpl userDetailsMock = Mockito.mock(UserDetailsImpl.class);
        Mockito.when(authMock.getPrincipal()).thenReturn(userDetailsMock);
        Mockito.when(userDetailsMock.getId()).thenReturn(1L);
        Mockito.when(userDetailsMock.getUsername()).thenReturn("testuser");
        Mockito.when(userDetailsMock.getEmail()).thenReturn("testuser@example.com");
        List list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        Mockito.when(userDetailsMock.getAuthorities())
                .thenReturn(list);
        // mock the JWT token generation
        String jwtMock = "generated-jwt-token";
        Mockito.when(jwtUtils.generateJwtToken(authMock)).thenReturn(jwtMock);

        // call the authenticateUser() method with the mock LoginRequest object
        ResponseEntity<?> responseEntity = controller.authenticateUser(validRequest);

        // assert that the response contains a valid JWT token and user details
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertEquals(jwtMock, jwtResponse.getAccessToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("testuser", jwtResponse.getUsername());
        assertEquals("testuser@example.com", jwtResponse.getEmail());
        assertEquals(Arrays.asList("ROLE_USER"), jwtResponse.getRoles());
    }

    @Test
    void testLogin() throws Exception {
        this.mockMvc.perform(formLogin()).andExpect(authenticated());
    }
}*/
