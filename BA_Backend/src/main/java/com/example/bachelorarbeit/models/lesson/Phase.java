package com.example.bachelorarbeit.models.lesson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="phases")
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phase_id", nullable = false)
    private Long phase_id;
    @ManyToOne
    @JoinColumn(name="lesson_id")
    @JsonBackReference
    private Lesson lesson;
    private String name;
    private int minutes;
    private String learningGoal;
    private String teacherAction;
    private String pupilAction;
    private String teachingForm;
    private String media;
    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<FileURI> fileURIs = new ArrayList<>();
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Long getPhase_id() {
        return phase_id;
    }

    public void setPhase_id(Long phase_id) {
        this.phase_id = phase_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getLearningGoal() {
        return learningGoal;
    }

    public void setLearningGoal(String learningGoal) {
        this.learningGoal = learningGoal;
    }

    public String getTeacherAction() {
        return teacherAction;
    }

    public void setTeacherAction(String teacherAction) {
        this.teacherAction = teacherAction;
    }

    public String getPupilAction() {
        return pupilAction;
    }

    public void setPupilAction(String pupilAction) {
        this.pupilAction = pupilAction;
    }

    public String getTeachingForm() {
        return teachingForm;
    }

    public void setTeachingForm(String teachingForm) {
        this.teachingForm = teachingForm;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public List<FileURI> getFileURIs() {
        return fileURIs;
    }

    public void setFileURIs(List<FileURI> fileURIs) {
        this.fileURIs = fileURIs;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void addFileURI(FileURI f) {
        fileURIs.add(f);
    }
}
