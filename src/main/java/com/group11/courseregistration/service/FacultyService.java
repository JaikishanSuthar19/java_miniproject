package com.group11.courseregistration.service;

import com.group11.courseregistration.data.DataStore;
import com.group11.courseregistration.model.Faculty;

import java.util.ArrayList;
import java.util.List;

public class FacultyService {

    public List<Faculty> getAllFaculties() {
        return new ArrayList<>(DataStore.faculties.values());
    }

    public Faculty getFaculty(String facultyId) {
        return DataStore.faculties.get(facultyId);
    }

    public List<Faculty> searchFaculties(String query) {
        List<Faculty> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Faculty faculty : DataStore.faculties.values()) {
            if (faculty.getFacultyId().toLowerCase().contains(lowerQuery) ||
                faculty.getFacultyName().toLowerCase().contains(lowerQuery) ||
                faculty.getDepartment().toLowerCase().contains(lowerQuery) ||
                faculty.getEmail().toLowerCase().contains(lowerQuery)) {
                results.add(faculty);
            }
        }
        return results;
    }

    public boolean addFaculty(Faculty faculty) {
        if (DataStore.faculties.containsKey(faculty.getFacultyId())) {
            return false;
        }
        DataStore.faculties.put(faculty.getFacultyId(), faculty);
        return true;
    }
}
