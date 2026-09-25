# Individual Project Report & Presentation Guide
## Member: Danish
**Group 11 — College Course Registration Management System**  
**Role**: Lead Architect — DataStore Architecture, Admin Dashboard & Faculty Management

---

## Executive Summary of Contribution

As part of Group 11, Danish was responsible for the foundational data architecture of the application: the **In-Memory DataStore Architecture implementing Java Collections**, the **Complete Faculty Management System**, and the **Administrative Dashboard & System-Wide Monitoring Portals**.

Key responsibilities included:
1. Architecting [DataStore.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/data/DataStore.java), integrating and seeding the 4 required Java Collections:
   - **`Course[]`** (Fixed-size Array)
   - **`LinkedList<Registration>`** (Doubly-Linked List)
   - **`HashMap<String, Student>`**, **`HashMap<String, Course>`**, **`HashMap<String, Faculty>`** (Hash Tables)
   - **`TreeMap<String, Course>`** (Red-Black Tree)
2. Implementing the **Faculty Management System** directly fulfilling the Problem Statement:
   - Entity model: [Faculty.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/model/Faculty.java)
   - Business service: [FacultyService.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/service/FacultyService.java)
   - Admin GUI: [AdminFacultyPanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminFacultyPanel.java) with live search and **"+ Add Faculty"** modal dialog.
3. Developing the [AdminDashboard](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminDashboard.java) shell with sidebar navigation, active button visual state highlighting, and automatic cross-panel data synchronization.
4. Designing the system overview [AdminHomePanel](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminHomePanel.java) featuring 5 live metric cards (Students, Courses, Faculty, Registrations, Open Seats) and Quick Action shortcut navigation.
5. Developing the student directory [AdminStudentPanel](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminStudentPanel.java) and the college-wide registration audit log [AdminRegistrationPanel](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminRegistrationPanel.java).

---

## Assigned Files & Technical Ownership

| File | Layer | Key Responsibilities |
|---|---|---|
| [DataStore.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/data/DataStore.java) | Data | Central in-memory data store holding Array, LinkedList, HashMaps, and TreeMap with initial seeding. |
| [Faculty.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/model/Faculty.java) | Model | Encapsulates faculty member properties: ID, instructor name, department, and contact email. |
| [FacultyService.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/service/FacultyService.java) | Service | Business logic for faculty queries, multi-attribute search, and duplicate ID prevention. |
| [AdminDashboard.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminDashboard.java) | GUI | Main administrative shell with `CardLayout`, active sidebar state, and tab auto-refresh dispatch. |
| [AdminHomePanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminHomePanel.java) | GUI | High-level system overview with 5 real-time metric cards and Quick Action buttons. |
| [AdminFacultyPanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminFacultyPanel.java) | GUI | Searchable faculty directory with an interactive modal dialog to add new faculty members. |
| [AdminStudentPanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminStudentPanel.java) | GUI | Directory of enrolled students with real-time multi-field search. |
| [AdminRegistrationPanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminRegistrationPanel.java) | GUI | College-wide audit table tracking all registration and cancellation transactions. |

---

## Detailed Technical Implementation

### 1. In-Memory DataStore Architecture (`DataStore.java`)
* Fulfills all 4 Collection requirements in the assignment problem statement:
```java
public class DataStore {
    // 1. Array: Fixed initial course storage (Objective 4)
    public static Course[] courses;

    // 2. LinkedList: Chronological registration event log (Objective 5)
    public static LinkedList<Registration> registrations;

    // 3. HashMap: O(1) constant time lookup by unique ID (Objective 6)
    public static HashMap<String, Student> students;
    public static HashMap<String, Course> courseMap;
    public static HashMap<String, Faculty> faculties;

    // 4. TreeMap: Self-sorting Red-Black Tree maintaining alphabetical Course IDs (Objective 7)
    public static TreeMap<String, Course> sortedCourses;

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
}
```

### 2. Faculty Management System (`FacultyService.java` & `AdminFacultyPanel.java`)
* The Problem Statement specifies: *"Develop a Java application to manage students, courses, **faculty**, course capacity, and student registrations."*
* Developed `FacultyService` with multi-field search and duplicate prevention:
```java
public boolean addFaculty(Faculty faculty) {
    if (DataStore.faculties.containsKey(faculty.getFacultyId())) {
        return false; // Prevent duplicate Faculty ID
    }
    DataStore.faculties.put(faculty.getFacultyId(), faculty);
    return true;
}

public List<Faculty> searchFaculties(String query) {
    List<Faculty> results = new ArrayList<>();
    String lower = query.toLowerCase();
    for (Faculty f : DataStore.faculties.values()) {
        if (f.getFacultyId().toLowerCase().contains(lower) ||
            f.getFacultyName().toLowerCase().contains(lower) ||
            f.getDepartment().toLowerCase().contains(lower) ||
            f.getEmail().toLowerCase().contains(lower)) {
            results.add(f);
        }
    }
    return results;
}
```

### 3. Admin Dashboard Shell & Tab State Synchronization (`AdminDashboard.java`)
* Maintains active sidebar button color state (`#2563EB`) and triggers data refresh when tabs are switched:
```java
public void switchTab(String name) {
    cardLayout.show(cardPanel, name);
    headerTitle.setText(name);
    updateNavSelection(name);

    // Auto-refresh target tab
    if ("Dashboard".equals(name)) {
        homePanel.refreshData();
    } else if ("Manage Courses".equals(name)) {
        coursePanel.loadData();
    } else if ("Students".equals(name)) {
        studentPanel.loadData("");
    } else if ("Faculty".equals(name)) {
        facultyPanel.loadData("");
    } else if ("Registrations".equals(name)) {
        registrationPanel.loadData();
    }
}
```

### 4. Admin Home Metric Aggregation (`AdminHomePanel.java`)
* Aggregates live numbers across all domain services into 5 visual metric cards:
```java
public void refreshData() {
    int totalStudents = studentService.getAllStudents().size();
    int totalCourses = courseService.getAllCourses().size();
    int totalFaculty = facultyService.getAllFaculties().size();
    int totalRegs = regService.getAllRegistrations().size();
    
    int availableSeats = 0;
    for (Course c : courseService.getAllCourses()) {
        availableSeats += c.getAvailableSeats();
    }

    studentsVal.setText(String.valueOf(totalStudents));
    coursesVal.setText(String.valueOf(totalCourses));
    facultyVal.setText(String.valueOf(totalFaculty));
    regsVal.setText(String.valueOf(totalRegs));
    seatsVal.setText(String.valueOf(availableSeats));
}
```

---

## Presentation & Viva Speaking Script

Use this script during your turn to present:

### 1. Introduction (30 seconds)
> *"Good morning/afternoon, teachers. My name is **Danish**, and in Group 11's Course Registration System, my primary role was designing the **In-Memory DataStore Architecture using Java Collections**, developing the **Faculty Management System**, and building the **Administrative Dashboard & Monitoring Portals**."*

### 2. Technical Walkthrough (1 - 2 minutes)
> *"At the foundation of our application is `DataStore.java`. I architected this class to implement the four required data structures from the problem statement:
> 1. A fixed array `Course[]` for the base course offerings.
> 2. A `LinkedList<Registration>` to maintain a chronological audit log of all registration events.
> 3. `HashMap` instances for Students, Courses, and Faculties, providing $O(1)$ constant time lookup by unique ID.
> 4. A `TreeMap<String, Course>`, which uses a self-balancing Red-Black Tree to keep courses sorted by ID for display.
>
> *I also noticed that the problem statement explicitly requires managing **faculty members**. To make our project 100% compliant, I developed `Faculty.java`, `FacultyService.java`, and `AdminFacultyPanel.java`. In the Admin portal, administrators can search faculty members by name or department, and register new professors using an interactive modal form.
>
> *Finally, in `AdminDashboard.java` and `AdminHomePanel.java`, I designed the system overview dashboard. It features five live metric cards that dynamically calculate total students, courses, faculty, registrations, and total available seats across all departments. I also implemented an active tab indicator on the sidebar and an auto-refresh dispatch so any changes made in other tabs reflect on the overview immediately."*

### 3. Live Demo Script (1 minute)
> *(While clicking on the running application)*
> 1. *"Let's sign in using the administrator account `admin` and `admin123`.*
> 2. *We arrive at the `AdminDashboard`. Notice the system overview cards: Total Students (4), Total Courses (6), Total Faculty (6), Total Registrations (6), and Open Seats (29).*
> 3. *Under Quick Actions, I can click 'Faculty Members'.*
> 4. *This opens `AdminFacultyPanel`. We can see our 6 professors with their department and email. If I type 'CSE' in the search bar, it instantly filters CSE faculty.*
> 5. *I can click '+ Add Faculty' to add a new instructor, for example 'Prof. Clark' in 'IT', which adds the faculty to our HashMap and immediately updates the table.*
> 6. *Next, let's look at 'Registrations'. Here is the complete audit log of all student transactions. Even when a student cancels a course, the transaction remains in this table marked with a red 'CANCELLED' badge, ensuring full institutional accountability."*

---

## ❓ Probable Viva Questions & Model Answers

### Q1: Why did you use `DataStore` with static collections instead of passing collections through constructors?
**Answer**:
> *"In a desktop GUI application without a database engine, `DataStore` acts as a centralized in-memory Repository / Singleton data layer. Using static references allows our multiple services (`StudentService`, `CourseService`, `FacultyService`, `RegistrationService`) to operate on the exact same shared data in memory without having to pass large object graphs through every Swing panel constructor."*

### Q2: How does `TreeMap` maintain sorted order in `DataStore`? What is its time complexity?
**Answer**:
> *"A `TreeMap` is an implementation of a NavigableMap based on a self-balancing Red-Black binary search tree. When we call `sortedCourses.put(courseId, course)`, the tree compares the new key against existing keys and inserts it into its proper alphabetical position. The time complexity for insertions, deletions, and lookups is $O(\log N)$, guaranteeing sorted output without having to manually call `Collections.sort()`."*

### Q3: How did you implement the Faculty Management System and why was it necessary?
**Answer**:
> *"The assignment Problem Statement explicitly says: 'Develop a Java application to manage students, courses, faculty, course capacity, and student registrations.' Initially, our codebase only had course and student views. I completed the requirement by creating `Faculty.java`, `FacultyService.java`, and `AdminFacultyPanel.java`, allowing administrators to view, search, and dynamically add faculty members with duplicate ID validation."*

### Q4: How does `AdminRegistrationPanel` track both active and cancelled registrations?
**Answer**:
> *"Our `RegistrationService` never deletes records from `DataStore.registrations`. When a cancellation occurs, the status field of that `Registration` object is updated to `'CANCELLED'`. In `AdminRegistrationPanel`, we query `registrationService.getAllRegistrations()`, which iterates over the entire `LinkedList` and renders green pills for `'REGISTERED'` and red pills for `'CANCELLED'`, giving administrators a complete chronological audit trail."*

### Q5: How do the Quick Action buttons on `AdminHomePanel` communicate with `AdminDashboard`?
**Answer**:
> *"I passed a reference of `AdminDashboard` into `AdminHomePanel`'s constructor (`parent`). When an administrator clicks a Quick Action button like 'Manage Courses' or 'Faculty Members', the button's `ActionListener` calls `parent.switchTab(\"Faculty\")`. This decouples the panel from the sidebar while allowing seamless programmatic navigation."*
