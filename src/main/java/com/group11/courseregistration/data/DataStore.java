package com.group11.courseregistration.data;

import com.group11.courseregistration.model.Course;
import com.group11.courseregistration.model.Registration;
import com.group11.courseregistration.model.Student;
import com.group11.courseregistration.model.Faculty;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class DataStore {

    // REQUIRED COLLECTIONS
    public static Course[] courses;
    public static LinkedList<Registration> registrations;
    public static HashMap<String, Student> students;
    public static HashMap<String, Course> courseMap;
    public static TreeMap<String, Course> sortedCourses;
    
    // Additional structure for faculties (optional but good for completeness)
    public static HashMap<String, Faculty> faculties;

    public static void initializeData() {
        courses = new Course[6];
        registrations = new LinkedList<>();
        students = new HashMap<>();
        courseMap = new HashMap<>();
        sortedCourses = new TreeMap<>();
        faculties = new HashMap<>();

        loadSampleFaculties();
        loadSampleStudents();
        loadSampleCourses();
        loadSampleRegistrations();
    }

    private static void loadSampleFaculties() {
        faculties.put("F001", new Faculty("F001", "Prof. Sharma", "CSE", "sharma@example.com"));
        faculties.put("F002", new Faculty("F002", "Prof. Patel", "CSE", "patel@example.com"));
        faculties.put("F003", new Faculty("F003", "Prof. Mehta", "IT", "mehta@example.com"));
        faculties.put("F004", new Faculty("F004", "Prof. Singh", "CSE", "singh@example.com"));
        faculties.put("F005", new Faculty("F005", "Prof. Kumar", "IT", "kumar@example.com"));
        faculties.put("F006", new Faculty("F006", "Prof. Joshi", "CSE", "joshi@example.com"));
    }

    private static void loadSampleStudents() {
        students.put("S001", new Student("S001", "Jaikishan", "jaiki@example.com", "student123", "CSE", 3));
        students.put("S002", new Student("S002", "Student Two", "student2@example.com", "student123", "CSE", 3));
        students.put("S003", new Student("S003", "Student Three", "student3@example.com", "student123", "IT", 3));
        
        // Also add logic to handle generic 'student' login from prompt demo accounts
        students.put("student", new Student("student", "Demo Student", "student@example.com", "student123", "CSE", 3));
    }

    private static void loadSampleCourses() {
        courses[0] = new Course("CS101", "Java Programming", "Prof. Sharma", 4, 40);
        courses[1] = new Course("CS102", "Data Structures", "Prof. Patel", 4, 40);
        courses[2] = new Course("CS103", "Database Management", "Prof. Mehta", 3, 35);
        courses[3] = new Course("CS104", "Computer Networks", "Prof. Singh", 3, 40);
        courses[4] = new Course("CS105", "Operating Systems", "Prof. Kumar", 4, 45);
        courses[5] = new Course("CS106", "Web Development", "Prof. Joshi", 3, 35);

        // Set registered counts manually for sample data
        courses[0].setRegisteredStudents(28);
        courses[1].setRegisteredStudents(35);
        courses[2].setRegisteredStudents(30);
        courses[3].setRegisteredStudents(40);
        courses[4].setRegisteredStudents(20);
        courses[5].setRegisteredStudents(18);

        for (Course course : courses) {
            courseMap.put(course.getCourseId(), course);
            sortedCourses.put(course.getCourseId(), course);
        }
    }

    private static void loadSampleRegistrations() {
        // R001, S001, CS101
        registrations.add(new Registration("R001", "S001", "CS101", LocalDate.of(2026, 9, 12), "REGISTERED"));
        // R002, S001, CS103
        registrations.add(new Registration("R002", "S001", "CS103", LocalDate.of(2026, 9, 14), "REGISTERED"));
        // R003, S001, CS106
        registrations.add(new Registration("R003", "S001", "CS106", LocalDate.of(2026, 9, 18), "REGISTERED"));
        
        // Add for generic student to match demo
        registrations.add(new Registration("R004", "student", "CS101", LocalDate.of(2026, 9, 12), "REGISTERED"));
        registrations.add(new Registration("R005", "student", "CS103", LocalDate.of(2026, 9, 14), "REGISTERED"));
        registrations.add(new Registration("R006", "student", "CS106", LocalDate.of(2026, 9, 18), "REGISTERED"));
    }
}
