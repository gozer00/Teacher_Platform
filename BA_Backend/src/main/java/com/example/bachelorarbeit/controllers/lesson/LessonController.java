package com.example.bachelorarbeit.controllers.lesson;

import com.example.bachelorarbeit.models.lesson.*;
import com.example.bachelorarbeit.payload.request.SaveLessonRequest;
import com.example.bachelorarbeit.payload.response.LessonMainInformation;
import com.example.bachelorarbeit.payload.response.MessageResponse;
import com.example.bachelorarbeit.repository.lesson.LessonRepository;
import com.example.bachelorarbeit.repository.lesson.PhaseRepository;
import com.example.bachelorarbeit.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private JwtUtils jwtUtils;
    @Autowired
    private PhaseRepository phaseRepository;

    /**
     * Endpoint for uploading new lessons.
     * @param token JWT
     * @param request SaveLessonRequest
     * @return ResponseEntity containing success message
     */
    @PostMapping("/create")
    public ResponseEntity<?> createLesson(@RequestHeader (name="Authorization") String token, @RequestBody SaveLessonRequest request) {
        saveLesson(request, token);
        return ResponseEntity.ok(new MessageResponse("Lesson saved successfully!"));
    }

    /**
     * This method extracts the fileURIs from the request and inserts them in the procedure plan
     * @param procedurePlan List of Phases
     * @param fileURIs List of RawURIs
     * @return List<Phase> representing a complete procedure plan
     */
    private List<Phase> createProcedurePlan(List<Phase> procedurePlan, List<RawURI> fileURIs) {
        for (RawURI f:fileURIs) {
            // Add fileURIs to procedure plan
                FileURI uri = new FileURI();
                uri.setUri(f.getUri());
                uri.setPhase(procedurePlan.get(f.getPhase()));
                procedurePlan.get(f.getPhase()).addFileURI(uri);
        }
        for (Phase p:procedurePlan) {
            // Map Phases to FileURIs
            for (FileURI f:p.getFileURIs()) {
                f.setPhase(p);
            }
        }
        return procedurePlan;
    }

    /**
     * Endpoint for getting Lessons found by their creator.
     * @param token JWT
     * @return Result list of lessons.
     */
    @GetMapping("/my")
    public List<Lesson> getMyLessons(@RequestHeader (name="Authorization") String token) {
        return lessonRepository.findByCreator(jwtUtils.getUserFromToken(token));
    }

    /**
     * Endpoint for getting a Lesson found by its id.
     * @param id id of the lesson
     * @return Lesson object
     * @throws Exception if lesson doesn't exist
     */
    @GetMapping("/{id}")
    public Lesson getLesson(@PathVariable Long id) throws Exception {
        return lessonRepository.findByLessonId(id)
                .orElseThrow(()->new Exception("Lesson doesn't exist"));
    }

    /**
     * Endpoint for getting Lessons found by their name or keywords using a query.
     * @param query String
     * @return LessonMainInformation containing name, subject and grade
     */
    @GetMapping("/search")
    public List<LessonMainInformation> searchLesson(@RequestParam String query) {
        // Load results from database
        List<Lesson> result = lessonRepository.searchLesson(query);
        ArrayList<LessonMainInformation> mainInformation = new ArrayList<>();
        for (Lesson l:result) {
            // Extract main information (capsule)
            mainInformation.add(new LessonMainInformation(l.getLessonId(),
                    l.getMetaInformation().getName(),
                    l.getMetaInformation().getSubject(),
                    l.getMetaInformation().getGrade()));
        }
        return mainInformation;
    }

    /**
     * Endpoint for updating Lessons.
     * @param token JWT
     * @param request SaveLessonRequest
     * @return ResponseEntity containing success or failure message
     * @throws Exception if updated lesson wasn't found
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateLesson(@RequestHeader (name="Authorization") String token,
                                          @RequestBody SaveLessonRequest request) throws Exception {
        // Load lessons from database
        Lesson lesson = lessonRepository.findByLessonId(request.getLessonId())
                .orElseThrow(() -> new Exception("Lesson not found"));

        // Update all values
        if(Objects.equals(lesson.getCreator().getId(), jwtUtils.getUserFromToken(token).getId())) {
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
            return ResponseEntity.badRequest().body(new MessageResponse("Lesson not saved!"));
        }
        return ResponseEntity.ok(new MessageResponse("Lesson saved successfully!"));
    }

    /**
     * Endpoint for deleting Lessons.
     * @param id id of the lesson to delete
     * @return ResponseEntity containing success message
     * @throws Exception if lesson with id doesn't exist
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) throws Exception {
        // TODO: 05.04.23 authority check
        Lesson lesson = lessonRepository.findByLessonId(id)
                .orElseThrow(()->new Exception("Lesson doesn't exist"));
        lessonRepository.delete(lesson);
        return ResponseEntity.ok("Deleted successfully");
    }

    /**
     * Service method that extracts all values of a Lesson from the requests and injects them into a Lesson object.
     * @param request SaveLessonRequest
     * @param token JWT
     */
    private void saveLesson(SaveLessonRequest request, String token) {
        // Create MetaInformation
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
        // Setup and save Lesson
        Lesson lesson = new Lesson(metaInformation, createProcedurePlan(request.getProcedurePlan(), request.getFileURIs()));
        lesson.setCreator(jwtUtils.getUserFromToken(token));
        lessonRepository.save(lesson);
    }

    /**
     * Service method that determines deleted phases and removes them from database
     * @param procedurePlan List<Phase> updated procedure plan
     * @param lesson Lesson object that is associated with the phases
     */
    private void handleDeletedPhases(List<Phase> procedurePlan, Lesson lesson){
        ArrayList<Long> phaseIds = new ArrayList<>();
        for (Phase p:procedurePlan) {
            if(p.getPhaseId() != null) {
                phaseIds.add(p.getPhaseId());
            }
        }
        phaseRepository.deleteRemovedPhases(phaseIds, lesson);
    }
}
