package com.example.bachelorarbeit.models.lesson;

import com.example.bachelorarbeit.models.user_management.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity(name = "lessons")
public class Lesson {

    public Lesson() {
    }

    public Lesson(MetaInformation meta, List<Phase> procedurePlan) {
        this.metaInformation = meta;
        updatePhases(procedurePlan);
        this.procedurePlan = procedurePlan;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="meta_id")
    @JsonManagedReference
    private MetaInformation metaInformation;


    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @JsonManagedReference
    @OrderBy("number ASC")
    private List<Phase> procedurePlan = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User creator;

    public List<Phase> getProcedurePlan() {
        return procedurePlan;
    }

    public void setProcedurePlan(List<Phase> procedurePlan) {
        this.procedurePlan = procedurePlan;
        for (Phase p:procedurePlan) {
            System.err.println(p.getName());
        }
        updatePhases(procedurePlan);
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lesson_id) {
        this.lessonId = lesson_id;
    }

    public MetaInformation getMetaInformation() {
        return metaInformation;
    }

    public void setMetaInformation(MetaInformation metaInformation) {
        this.metaInformation = metaInformation;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    private void updatePhases(List<Phase> procedurePlan) {
        AtomicInteger i = new AtomicInteger();
        procedurePlan.forEach(phase -> {
            phase.setLesson(this);
            phase.setNumber(i.get());
            i.set(i.get() + 1);
        });
    }
}
