package com.group11.courseregistration.model;

public class Course {
    private String courseId;
    private String courseName;
    private String facultyName;
    private int credits;
    private int capacity;
    private int registeredStudents;

    public Course(String courseId, String courseName, String facultyName, int credits, int capacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.facultyName = facultyName;
        this.credits = credits;
        this.capacity = capacity;
        this.registeredStudents = 0;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRegisteredStudents() {
        return registeredStudents;
    }

    public void setRegisteredStudents(int registeredStudents) {
        this.registeredStudents = registeredStudents;
    }

    public int getAvailableSeats() {
        return capacity - registeredStudents;
    }

    public boolean isFull() {
        return registeredStudents >= capacity;
    }

    public void registerStudent() {
        if (!isFull()) {
            registeredStudents++;
        }
    }

    public void removeStudent() {
        if (registeredStudents > 0) {
            registeredStudents--;
        }
    }
}
