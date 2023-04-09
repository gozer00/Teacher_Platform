package com.example.bachelorarbeit;

import com.example.bachelorarbeit.controllers.lesson.LessonController;
import com.example.bachelorarbeit.models.lesson.MetaInformation;
import com.example.bachelorarbeit.models.lesson.Phase;
import com.example.bachelorarbeit.models.lesson.Lesson;
import com.example.bachelorarbeit.models.lesson.RawURI;
import com.example.bachelorarbeit.models.user_management.User;
import com.example.bachelorarbeit.payload.request.SaveLessonRequest;
import com.example.bachelorarbeit.repository.lesson.LessonRepository;
import com.example.bachelorarbeit.repository.lesson.PhaseRepository;
import com.example.bachelorarbeit.repository.user_management.UserRepository;
import com.example.bachelorarbeit.security.jwt.JwtUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LessonControllerUpdateLessonTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhaseRepository phaseRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private LessonController lessonController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }


    /**
     * Test method for updating a lesson.
     * @throws Exception
     */
    @Test
    public void testUpdateLesson() throws Exception {
        // setup the request body
        SaveLessonRequest request = new SaveLessonRequest();
        request.setLessonId(1L);
        request.setName("Lesson");
        request.setSubject("Math");
        request.setGrade(5);
        request.setSchool("School");
        request.setState("State");
        request.setLessonThema("Thema");
        request.setMedia("Media");
        request.setLessonType("Lesson Type");
        request.setLearningGoals("Learning Goals");
        request.setPreKnowledge("Pre-Knowledge");
        request.setResources("Resources");
        request.setKeywords("Keywords");
        request.setpPublic(true);
        List<Phase> procedurePlan = new ArrayList<>();
        procedurePlan.add(new Phase());
        procedurePlan.add(new Phase());
        request.setProcedurePlan(procedurePlan);
        List<RawURI> uris = new ArrayList<>();
        request.setFileURIs(uris);

        // mock user
        User user = new User();
        when(userRepository.findByUsername(null)).thenReturn(Optional.of(user));

        // create lesson
        Lesson lesson = new Lesson();
        lesson.setLessonId(1L);
        lesson.setCreator(user);
        lesson.setMetaInformation(new MetaInformation());
        when(lessonRepository.findByLessonId(1L)).thenReturn(Optional.of(lesson));

        // execute endpoint access
        mockMvc.perform(put("/update")
                        .header("Authorization", "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.message").value("Lesson saved successfully!"));

        // verification
        verify(lessonRepository).findByLessonId(1L);
        verify(lessonRepository).save(lesson);
        verify(phaseRepository).deleteRemovedPhases(Arrays.asList(1L, 2L), lesson);
        assertEquals(request.getName(), lesson.getMetaInformation().getName());
        assertEquals(request.getSubject(), lesson.getMetaInformation().getSubject());
        assertEquals(request.getGrade(), lesson.getMetaInformation().getGrade());
        assertEquals(request.getSchool(), lesson.getMetaInformation().getSchool());
        assertEquals(request.getState(), lesson.getMetaInformation().getState());
        assertEquals(request.getLessonThema(), lesson.getMetaInformation().getLessonThema());
        assertEquals(request.getMedia(), lesson.getMetaInformation().getMedia());
        assertEquals(request.getLessonType(), lesson.getMetaInformation().getLessonType());
        assertEquals(request.getLearningGoals(), lesson.getMetaInformation().getLearningGoals());
        assertEquals(request.getPreKnowledge(), lesson.getMetaInformation().getPreKnowledge());
        assertEquals(request.getResources(), lesson.getMetaInformation().getResources());
    }
}
