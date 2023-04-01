package com.example.bachelorarbeit.controllers.lesson;

import com.example.bachelorarbeit.models.lesson.*;
import com.example.bachelorarbeit.models.user_management.User;
import com.example.bachelorarbeit.payload.request.SaveLessonRequest;
import com.example.bachelorarbeit.payload.response.LessonMainInformation;
import com.example.bachelorarbeit.payload.response.MessageResponse;
import com.example.bachelorarbeit.repository.lesson.LessonRepository;
import com.example.bachelorarbeit.repository.lesson.PhaseRepository;
import com.example.bachelorarbeit.repository.user_management.UserRepository;
import com.example.bachelorarbeit.security.jwt.AuthTokenFilter;
import com.example.bachelorarbeit.security.jwt.JwtUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/lessons")
public class LessonController {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PhaseRepository phaseRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createLesson(@RequestHeader (name="Authorization") String token, @RequestBody SaveLessonRequest request) {
        saveLesson(request, token);
        return ResponseEntity.ok(new MessageResponse("Lesson saved successfully!"));
    }

    private List<Phase> createProcedurePlan(List<Phase> procedurePlan, List<RawURI> fileURIs) {
        for (RawURI f:fileURIs) {
                FileURI uri = new FileURI();
                uri.setUri(f.getUri());
                uri.setPhase(procedurePlan.get(f.getPhase()));
                procedurePlan.get(f.getPhase()).addFileURI(uri);
        }
        for (Phase p:procedurePlan) {
            for (FileURI f:p.getFileURIs()) {
                f.setPhase(p);
            }
        }
        return procedurePlan;
    }

    @GetMapping("/my")
    public List<Lesson> getMyLessons(@RequestHeader (name="Authorization") String token) {  //todo: make error handling right
        return lessonRepository.findByCreator(getUserFromToken(token));
    }

    @GetMapping("/{id}")
    public Lesson getLesson(@PathVariable Long id) throws Exception {
        return lessonRepository.findByLessonId(id)
                .orElseThrow(()->new Exception("Lesson doesn't exist"));
    }

    @GetMapping("/search")
    public List<LessonMainInformation> searchLesson(@RequestParam String query) {
        List<Lesson> result = lessonRepository.searchLesson(query);
        ArrayList<LessonMainInformation> mainInformation = new ArrayList<>();
        for (Lesson l:result) {
            mainInformation.add(new LessonMainInformation(l.getLessonId(),
                    l.getMetaInformation().getName(),
                    l.getMetaInformation().getSubject(),
                    l.getMetaInformation().getGrade()));
        }
        return mainInformation;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLesson(@RequestHeader (name="Authorization") String token,
                                          @RequestBody SaveLessonRequest request) throws Exception {
        System.out.println(new Gson().toJson(request));
        Lesson lesson = lessonRepository.findByLessonId(request.getLessonId())
                .orElseThrow(() -> new Exception("Lesson not found"));
        if(Objects.equals(lesson.getCreator().getUser_id(), getUserFromToken(token).getUser_id())) {
            MetaInformation meta = lesson.getMetaInformation();
            meta.setName(request.getName());
            meta.setSubject(request.getSubject());
            meta.setGrade(request.getGrade());
            meta.setSchool(request.getSchool());
            meta.setState(request.getState());
            meta.setLessonThema(request.getLessonThema());
            meta.setMedia(request.getMedia());
            meta.setLessonType(request.getLessonType());
            meta.setLearningGoals(request.getLearningGoals());
            meta.setPreKnowledge(request.getPreKnowledge());
            meta.setResources(request.getResources());
            meta.setKeywords(request.getKeywords());
            meta.setPublic(request.ispPublic());
            handleDeletedPhases(request.getProcedurePlan(), lesson);
            lesson.setProcedurePlan(createProcedurePlan(request.getProcedurePlan(), request.getFileURIs()));
            lessonRepository.save(lesson);
        } else {
            return ResponseEntity.ok(new MessageResponse("Lesson not saved!"));
        }
        return ResponseEntity.ok(new MessageResponse("Lesson saved successfully!"));
    }

    private void handleDeletedPhases(List<Phase> procedurePlan, Lesson lesson){
        ArrayList<Long> phaseIds = new ArrayList<>();
        for (Phase p:procedurePlan) {
            if(p.getPhaseId() != null) {
                phaseIds.add(p.getPhaseId());
            }
        }
        phaseRepository.deleteRemovedPhases(phaseIds, lesson);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) throws Exception {
        Lesson lesson = lessonRepository.findByLessonId(id)
                .orElseThrow(()->new Exception("Lesson doesn't exist"));
        lessonRepository.delete(lesson);
        return ResponseEntity.ok("Deleted successfully");
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

    private void saveLesson(SaveLessonRequest request, String token) {
        MetaInformation metaInformation = new MetaInformation(
                request.getName(),
                request.getSubject(),
                request.getGrade(),
                request.getSchool(),
                request.getState(),
                request.getLessonThema(),
                request.getMedia(),
                request.getLessonType(),
                request.getLearningGoals(),
                request.getPreKnowledge(),
                request.getResources(),
                request.getKeywords(),
                request.ispPublic()
        );
        Lesson lesson = new Lesson(metaInformation, createProcedurePlan(request.getProcedurePlan(), request.getFileURIs()));
        lesson.setCreator(getUserFromToken(token));

        lessonRepository.save(lesson);
    }
}
