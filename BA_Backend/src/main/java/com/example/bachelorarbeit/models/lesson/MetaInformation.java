package com.example.bachelorarbeit.models.lesson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.util.ArrayList;

@Entity
@Table(name = "meta_information")
public class MetaInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meta_id", nullable = false)
    private Long meta_id;

    @OneToOne(mappedBy = "metaInformation")
    @JsonBackReference
    private Lesson lesson;

    @NotBlank
    private String name;
    @NotBlank
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

    private boolean isPublic;

    public MetaInformation(String name,
                           String subject,
                           int grade,
                           String school,
                           String state,
                           String lessonThema,
                           String media,
                           String lessonType,
                           String learningGoals,
                           String preKnowledge,
                           String resources,
                           String keywords,
                           boolean isPublic) {
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
        this.isPublic = isPublic;
    }

    public MetaInformation() {

    }

    public Long getMeta_id() {
        return meta_id;
    }

    public void setMeta_id(Long meta_id) {
        this.meta_id = meta_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        this.isPublic = aPublic;
    }
}
