package com.group11.courseregistration.model;

public class Student {
    private String studentId;
    private String studentName;
    private String email;
    private String password;
    private String department;
    private int semester;

    public Student(String studentId, String studentName, String email, String password, String department, int semester) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.semester = semester;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
