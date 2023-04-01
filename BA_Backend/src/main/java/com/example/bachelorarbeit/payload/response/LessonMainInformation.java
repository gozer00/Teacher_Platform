package com.example.bachelorarbeit.payload.response;

public class LessonMainInformation {
    public LessonMainInformation(long lessonId, String name, String subject, int grade) {
        this.lessonId = lessonId;
        this.name = name;
        this.subject = subject;
        this.grade = grade;
    }
    private long lessonId;
    private String name;
    private String subject;
    private int grade;

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
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
}
