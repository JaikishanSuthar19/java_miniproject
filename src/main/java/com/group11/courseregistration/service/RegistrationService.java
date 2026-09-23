package com.group11.courseregistration.service;

import com.group11.courseregistration.data.DataStore;
import com.group11.courseregistration.model.Course;
import com.group11.courseregistration.model.Registration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistrationService {

    public enum RegistrationResult {
        SUCCESS, DUPLICATE, FULL, NOT_FOUND
    }

    public RegistrationResult registerStudent(String studentId, String courseId) {
        Course course = DataStore.courseMap.get(courseId);
        if (course == null) return RegistrationResult.NOT_FOUND;

        if (isAlreadyRegistered(studentId, courseId)) {
            return RegistrationResult.DUPLICATE;
        }

        if (course.isFull()) {
            return RegistrationResult.FULL;
        }

        course.registerStudent();
        
        String regId = "R" + (DataStore.registrations.size() + 100); // simple ID generation
        Registration reg = new Registration(regId, studentId, courseId, LocalDate.now(), "REGISTERED");
        DataStore.registrations.add(reg);
        
        return RegistrationResult.SUCCESS;
    }

    public boolean cancelRegistration(String registrationId) {
        for (Registration reg : DataStore.registrations) {
            if (reg.getRegistrationId().equals(registrationId) && reg.getStatus().equals("REGISTERED")) {
                reg.setStatus("CANCELLED");
                
                Course course = DataStore.courseMap.get(reg.getCourseId());
                if (course != null) {
                    course.removeStudent();
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean cancelRegistrationByStudentAndCourse(String studentId, String courseId) {
        for (Registration reg : DataStore.registrations) {
            if (reg.getStudentId().equals(studentId) && reg.getCourseId().equals(courseId) && reg.getStatus().equals("REGISTERED")) {
                reg.setStatus("CANCELLED");
                
                Course course = DataStore.courseMap.get(courseId);
                if (course != null) {
                    course.removeStudent();
                }
                return true;
            }
        }
        return false;
    }

    private boolean isAlreadyRegistered(String studentId, String courseId) {
        for (Registration reg : DataStore.registrations) {
            if (reg.getStudentId().equals(studentId) && reg.getCourseId().equals(courseId) && reg.getStatus().equals("REGISTERED")) {
                return true;
            }
        }
        return false;
    }

    public List<Registration> getStudentRegistrations(String studentId) {
        List<Registration> results = new ArrayList<>();
        for (Registration reg : DataStore.registrations) {
            if (reg.getStudentId().equals(studentId) && reg.getStatus().equals("REGISTERED")) {
                results.add(reg);
            }
        }
        return results;
    }

    public List<Registration> getAllRegistrations() {
        return new ArrayList<>(DataStore.registrations);
    }
    
    public int getStudentTotalCredits(String studentId) {
        int total = 0;
        List<Registration> regs = getStudentRegistrations(studentId);
        for (Registration reg : regs) {
            Course c = DataStore.courseMap.get(reg.getCourseId());
            if (c != null) {
                total += c.getCredits();
            }
        }
        return total;
    }
}
