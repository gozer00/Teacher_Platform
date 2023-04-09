package com.example.bachelorarbeit;

import com.example.bachelorarbeit.controllers.lesson.LessonController;
import com.example.bachelorarbeit.models.lesson.*;
import com.example.bachelorarbeit.models.user_management.User;
import com.example.bachelorarbeit.payload.request.SaveLessonRequest;
import com.example.bachelorarbeit.payload.response.LessonMainInformation;
import com.example.bachelorarbeit.payload.response.MessageResponse;
import com.example.bachelorarbeit.repository.lesson.LessonRepository;
import com.example.bachelorarbeit.repository.user_management.UserRepository;
import com.example.bachelorarbeit.security.jwt.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerTest {

    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private LessonController lessonController;
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for delete lesson.
     * @throws Exception
     */
    @Test
    public void testDeleteLesson() throws Exception {
        // create Lesson object
        Long id = 1L;
        Lesson lesson = new Lesson();
        lesson.setLessonId(id);
        when(lessonRepository.findByLessonId(id)).thenReturn(Optional.of(lesson));

        // load answer
        ResponseEntity<?> responseEntity = lessonController.deleteLesson(id);

        // verification
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getBody(), "Deleted successfully");
    }

    /**
     * Test method for delete lesson when lesson was not found
     * @throws Exception
     */
    @Test
    public void testDeleteLesson_whenLessonNotFound() throws Exception {
        // setup
        Long id = 1L;
        when(lessonRepository.findByLessonId(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            lessonController.deleteLesson(id);
        });

        // compare
        String expectedMessage = "Lesson doesn't exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    /**
     * Test method for searchLesson function
     */
    @Test
    public void testSearchLesson() {
        // save two lessons and mock the result
        String query = "mathe";
        List<Lesson> mockResult = new ArrayList<>();
        MetaInformation metaInformation1 = new MetaInformation();
        List<Phase> procedurePlan1 = new ArrayList<>();
        Lesson lesson1 = new Lesson(metaInformation1, procedurePlan1);
        lesson1.setLessonId(1L);
        lesson1.getMetaInformation().setName("Mathe 1");
        lesson1.getMetaInformation().setSubject("Mathematik");
        lesson1.getMetaInformation().setGrade(9);
        MetaInformation metaInformation2 = new MetaInformation();
        List<Phase> procedurePlan2 = new ArrayList<>();
        Lesson lesson2 = new Lesson(metaInformation2, procedurePlan2);
        lesson2.setLessonId(2L);
        lesson2.getMetaInformation().setName("Mathe 2");
        lesson2.getMetaInformation().setSubject("Mathematik");
        lesson2.getMetaInformation().setGrade(10);
        mockResult.add(lesson1);
        mockResult.add(lesson2);
        when(lessonRepository.searchLesson(query)).thenReturn(mockResult);

        // execute endpoint access
        List<LessonMainInformation> result = lessonController.searchLesson(query);

        // verification
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getLessonId(), lesson1.getLessonId());
        assertEquals(result.get(0).getName(), lesson1.getMetaInformation().getName());
        assertEquals(result.get(0).getSubject(), lesson1.getMetaInformation().getSubject());
        assertEquals(result.get(0).getGrade(), lesson1.getMetaInformation().getGrade());
        assertEquals(result.get(1).getLessonId(), lesson2.getLessonId());
        assertEquals(result.get(1).getName(), lesson2.getMetaInformation().getName());
        assertEquals(result.get(1).getSubject(), lesson2.getMetaInformation().getSubject());
        assertEquals(result.get(1).getGrade(), lesson2.getMetaInformation().getGrade());
    }

    /**
     * Test method for getLesson
     * @throws Exception
     */
    @Test
    public void testGetLesson() throws Exception {
        Long lessonId = 1L;
        Lesson lesson = new Lesson();
        lesson.setLessonId(lessonId);
        when(lessonRepository.findByLessonId(lessonId)).thenReturn(Optional.of(lesson));
        Lesson result = lessonController.getLesson(lessonId);
        assertEquals(result.getLessonId(), lessonId);
    }

    /**
     * Test method for getLesson and Lesson not found
     */
    @Test
    public void testGetLessonLessonNotFound() {
        Long lessonId = 1L;
        when(lessonRepository.findByLessonId(lessonId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            lessonController.getLesson(lessonId);
        });
        assertEquals(exception.getMessage(), "Lesson doesn't exist");
    }

    /**
     * Test method for creation of a new Lesson
     */
    @Test
    public void testCreateLesson() {
        User user = new User();
        String name = "Mathe 1";
        String subject = "Mathematik";
        int grade = 9;
        String school = "Gymnasium";
        String state = "BW";
        List<Phase> procedurePlan = new ArrayList();
        List<RawURI> uris = new ArrayList<>();
        long userId = 1;
        long lessonId = 1;
        SaveLessonRequest request = new SaveLessonRequest(name, subject, grade, school, state, "", "",
                "", "", "", "", "", false, procedurePlan,
                uris, userId, lessonId);
        when(userRepository.findByUsername(null)).thenReturn(Optional.of(user));
        ResponseEntity<?> responseEntity = lessonController.createLesson("", request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Lesson saved successfully!", ((MessageResponse)responseEntity.getBody()).getMessage());
    }

    /**
     * Test method for getMyLessons
     */
    @Test
    public void testGetMyLessons() {
        User user1 = new User();
        List<Lesson> expectedLessons = new ArrayList<>();
        Lesson lesson1 = new Lesson();
        Lesson lesson2 = new Lesson();
        expectedLessons.add(lesson1);
        expectedLessons.add(lesson2);
        when(userRepository.findByUsername(null)).thenReturn(Optional.of(user1));
        when(lessonRepository.findByCreator(user1)).thenReturn(expectedLessons);
        List<Lesson> actualLessons = lessonController.getMyLessons("");
        assertEquals(expectedLessons, actualLessons);
    }
}