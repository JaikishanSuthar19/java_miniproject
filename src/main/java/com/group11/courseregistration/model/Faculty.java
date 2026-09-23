package com.group11.courseregistration.model;

public class Faculty {
    private String facultyId;
    private String facultyName;
    private String department;
    private String email;

    public Faculty(String facultyId, String facultyName, String department, String email) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.department = department;
        this.email = email;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
