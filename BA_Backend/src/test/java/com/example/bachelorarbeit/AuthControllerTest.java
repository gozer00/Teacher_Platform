package com.example.bachelorarbeit;

import com.example.bachelorarbeit.models.user_management.ERole;
import com.example.bachelorarbeit.models.user_management.User;
import com.example.bachelorarbeit.payload.request.LoginRequest;
import com.example.bachelorarbeit.payload.request.SignupRequest;
import com.example.bachelorarbeit.repository.user_management.RoleRepository;
import com.example.bachelorarbeit.repository.user_management.UserRepository;
import com.example.bachelorarbeit.security.jwt.JwtUtils;
import com.example.bachelorarbeit.security.services.UserDetailsImpl;
import com.example.bachelorarbeit.payload.response.JwtResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final String AUTHENTICATION_ENDPOINT = "/api/auth/signin";
    private static final String REGISTRATION_ENDPOINT = "/api/auth/signup";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testSignInValidCredentials() throws Exception {
        String username = "testuser";
        String email = "mail";
        String password = "testpassword";
        User user = new User(username, email, password);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest(username, password);

        MvcResult result = mockMvc.perform(post(AUTHENTICATION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponse jwtResponse = gson.fromJson(result.getResponse().getContentAsString(), JwtResponse.class);
        String jwt = jwtResponse.getAccessToken();
        String userName = jwtUtils.getUserNameFromJwtToken(jwt);
        assertEquals(userName, userName);
    }

    @Test
    public void testSignInInvalidCredentials() throws Exception {
        String username = "testuser";
        String email = "mail";
        String password = "testpassword";
        User user = new User(username, email, password);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest(username, "invalidpassword");

        mockMvc.perform(post(AUTHENTICATION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSignUpValidCredentials() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setEmail("testuser@example.com");
        signupRequest.setPassword("testpassword");
        signupRequest.setRole(Collections.singleton("user"));

        mockMvc.perform(post(REGISTRATION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(signupRequest)))
                .andExpect(status().isOk());

        User user = userRepository.findByUsername("testuser").get();
        assertEquals("testuser@example.com", user.getEmail());
        assertTrue(passwordEncoder.matches("testpassword", user.getPassword()));
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_USER));
    }

    @Test
    public void testSignUpDuplicateUsername() throws Exception {
        // given
        String username = "testuser";
        String email = "mail";
        String password = "testpassword";
        User user = new User(username, email, password);
        userRepository.save(user);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setEmail("testuser2@example.com");
        signupRequest.setPassword("testpassword2");
        signupRequest.setRole(Collections.singleton("user"));

        mockMvc.perform(post(REGISTRATION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(signupRequest)))
                .andExpect(status().isBadRequest());
    }
}
