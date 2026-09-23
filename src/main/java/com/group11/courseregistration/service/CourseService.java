package com.group11.courseregistration.service;

import com.group11.courseregistration.data.DataStore;
import com.group11.courseregistration.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseService {

    public List<Course> getAllCourses() {
        return new ArrayList<>(DataStore.courseMap.values());
    }

    public List<Course> getSortedCourses() {
        return new ArrayList<>(DataStore.sortedCourses.values());
    }

    public Course getCourse(String courseId) {
        return DataStore.courseMap.get(courseId);
    }

    public List<Course> searchCourses(String query) {
        List<Course> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Course course : DataStore.sortedCourses.values()) {
            if (course.getCourseId().toLowerCase().contains(lowerQuery) ||
                course.getCourseName().toLowerCase().contains(lowerQuery) ||
                course.getFacultyName().toLowerCase().contains(lowerQuery)) {
                results.add(course);
            }
        }
        return results;
    }

    public boolean addCourse(Course course) {
        if (DataStore.courseMap.containsKey(course.getCourseId())) {
            return false; // Duplicate
        }
        
        // Find empty slot in array (if we want to maintain the array constraint strictly)
        boolean addedToArray = false;
        for (int i = 0; i < DataStore.courses.length; i++) {
            if (DataStore.courses[i] == null) {
                DataStore.courses[i] = course;
                addedToArray = true;
                break;
            }
        }
        
        // If array is full, we could resize it, but for simplicity let's just resize it manually
        if (!addedToArray) {
            Course[] newArray = new Course[DataStore.courses.length * 2];
            System.arraycopy(DataStore.courses, 0, newArray, 0, DataStore.courses.length);
            newArray[DataStore.courses.length] = course;
            DataStore.courses = newArray;
        }

        DataStore.courseMap.put(course.getCourseId(), course);
        DataStore.sortedCourses.put(course.getCourseId(), course);
        return true;
    }

    public boolean updateCourse(Course updatedCourse) {
        if (!DataStore.courseMap.containsKey(updatedCourse.getCourseId())) {
            return false;
        }
        
        Course existing = DataStore.courseMap.get(updatedCourse.getCourseId());
        if (updatedCourse.getCapacity() < existing.getRegisteredStudents()) {
            return false; // Cannot reduce capacity below registered count
        }
        
        existing.setCourseName(updatedCourse.getCourseName());
        existing.setFacultyName(updatedCourse.getFacultyName());
        existing.setCredits(updatedCourse.getCredits());
        existing.setCapacity(updatedCourse.getCapacity());
        
        return true;
    }

    public void deleteCourse(String courseId) {
        DataStore.courseMap.remove(courseId);
        DataStore.sortedCourses.remove(courseId);
        
        // Remove from array
        for (int i = 0; i < DataStore.courses.length; i++) {
            if (DataStore.courses[i] != null && DataStore.courses[i].getCourseId().equals(courseId)) {
                DataStore.courses[i] = null;
                break;
            }
        }
    }
}
