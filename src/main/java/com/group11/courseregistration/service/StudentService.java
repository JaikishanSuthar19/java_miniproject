package com.group11.courseregistration.service;

import com.group11.courseregistration.data.DataStore;
import com.group11.courseregistration.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentService {
    
    public Student login(String username, String password) {
        Student student = DataStore.students.get(username);
        if (student != null && student.getPassword().equals(password)) {
            return student;
        }
        return null;
    }

    public Student getStudent(String studentId) {
        return DataStore.students.get(studentId);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(DataStore.students.values());
    }

    public List<Student> searchStudents(String query) {
        List<Student> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Student student : DataStore.students.values()) {
            if (student.getStudentId().toLowerCase().contains(lowerQuery) ||
                student.getStudentName().toLowerCase().contains(lowerQuery) ||
                student.getDepartment().toLowerCase().contains(lowerQuery)) {
                results.add(student);
            }
        }
        return results;
    }
}
