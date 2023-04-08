package com.example.bachelorarbeit.payload.request;

import com.example.bachelorarbeit.models.lesson.FileURI;
import com.example.bachelorarbeit.models.lesson.Phase;
import com.example.bachelorarbeit.models.lesson.RawURI;

import java.util.List;
import java.util.Set;

public class SaveLessonRequest {
    private String name;
    private String subject;
    private int grade;
    private String school;
    private String state;
    private String lessonThema;
    private String media;
    private String lessonType;
    private String learningGoals;
    private String preKnowledge;
    private String resources;
    private String keywords;
    private boolean pPublic;
    private List<Phase> procedurePlan;

    private List<RawURI> fileURIs;
    private Long userId;

    private Long lessonId;

    public SaveLessonRequest(String name, String subject, int grade, String school, String state, String lessonThema, String media, String lessonType, String learningGoals, String preKnowledge, String resources, String keywords, boolean pPublic, List<Phase> procedurePlan, List<RawURI> fileURIs, Long userId, Long lessonId) {
        this.name = name;
        this.subject = subject;
        this.grade = grade;
        this.school = school;
        this.state = state;
        this.lessonThema = lessonThema;
        this.media = media;
        this.lessonType = lessonType;
        this.learningGoals = learningGoals;
        this.preKnowledge = preKnowledge;
        this.resources = resources;
        this.keywords = keywords;
        this.pPublic = pPublic;
        this.procedurePlan = procedurePlan;
        this.fileURIs = fileURIs;
        this.userId = userId;
        this.lessonId = lessonId;
    }

    public SaveLessonRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLessonThema() {
        return lessonThema;
    }

    public void setLessonThema(String lessonThema) {
        this.lessonThema = lessonThema;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public String getLearningGoals() {
        return learningGoals;
    }

    public void setLearningGoals(String learningGoals) {
        this.learningGoals = learningGoals;
    }

    public String getPreKnowledge() {
        return preKnowledge;
    }

    public void setPreKnowledge(String preKnowledge) {
        this.preKnowledge = preKnowledge;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean ispPublic() {
        return pPublic;
    }

    public void setpPublic(boolean isPublic) {
        this.pPublic = isPublic;
    }

    public List<Phase> getProcedurePlan() {
        return procedurePlan;
    }

    public void setProcedurePlan(List<Phase> procedurePlan) {
        this.procedurePlan = procedurePlan;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<RawURI> getFileURIs() {
        return fileURIs;
    }

    public void setFileURIs(List<RawURI> fileURIs) {
        this.fileURIs = fileURIs;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }
}
