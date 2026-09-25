# Complete Code Explanation & Architectural Guide
## College Course Registration Management System (Mini Project 11 - Group 11)

---

## 1. Project Overview & Mapping to Problem Statement

### Problem Statement Requirements:
1. **Manage Student & Course Information**: Full model and service layers for Student and Course records.
2. **Manage Faculty Information**: Manage and display Faculty details linked with course allocations via dedicated service and Admin Faculty portal.
3. **Manage Course Registrations**: Real-time course enrollment, active tracking, and cancellations.
4. **Monitor Course Capacity**: Strict validation preventing over-enrollment (`capacity` vs `registeredStudents`).
5. **Data Structures Utilized**:
   - **Array (`Course[]`)**: Fixed storage for base course offerings.
   - **LinkedList (`LinkedList<Registration>`)**: Chronological audit log of registrations and cancellations.
   - **HashMap (`HashMap<String, Student>`, `HashMap<String, Course>`, `HashMap<String, Faculty>`)**: $O(1)$ constant-time lookup by unique IDs.
   - **TreeMap (`TreeMap<String, Course>`)**: Self-balancing Red-Black Tree maintaining courses in alphabetical order by ID.
6. **Modern Swing GUI**: Dual-role dashboards for Students and Administrators with FlatLaf styling, live tab data synchronization, active navigation states, and keyboard shortcuts.

---

## 2. Directory Structure

```text
com.group11.courseregistration
├── Main.java                        # Application entry point & Look-and-Feel initialization
├── model
│   ├── Course.java                  # Course entity with seat & capacity management
│   ├── Student.java                 # Student entity with profile and credentials
│   ├── Faculty.java                 # Faculty entity with department details
│   └── Registration.java            # Registration transaction entity with status & timestamp
├── data
│   └── DataStore.java               # In-memory store holding the 4 required data structures
├── service
│   ├── CourseService.java           # Course business logic (search, CRUD, array & map syncing)
│   ├── StudentService.java          # Student authentication and search operations
│   ├── FacultyService.java          # Faculty directory management and search operations
│   └── RegistrationService.java     # Enrollment rules, credit limits, duplicate checks, cancellations
├── utils
│   └── UIUtils.java                 # Central design system (colors, fonts, component factories)
└── gui
    ├── LoginFrame.java              # Dual-portal login interface with Enter key support
    ├── StudentDashboard.java        # Student shell with sidebar navigation & CardLayout auto-refresh
    ├── StudentHomePanel.java        # Student overview metrics and live recent registrations
    ├── CoursePanel.java             # Available courses browser with live search & capacity badges
    ├── RegistrationPanel.java       # Interactive registration card with credit cap & seat checks
    ├── MyCoursesPanel.java          # Enrolled courses table with interactive cancellation button
    ├── AdminDashboard.java          # Admin shell with sidebar navigation & live view synchronization
    ├── AdminHomePanel.java          # Admin overview metrics (Students, Courses, Faculty, Seats)
    ├── AdminCoursePanel.java        # Admin course CRUD panel with modal Add/Edit & Safe Deletion
    ├── AdminStudentPanel.java       # Directory of students with live search
    ├── AdminFacultyPanel.java       # Faculty directory with live search and Add Faculty dialog
    └── AdminRegistrationPanel.java  # System-wide audit log of all registration activities
```

---

## 3. Comprehensive Line-by-Line Code Breakdown

---

### File 1: `Main.java`
**Location**: `src/main/java/com/group11/courseregistration/Main.java`  
**Purpose**: Initializes Look & Feel, seeds in-memory data, and displays the login window on the Swing Event Dispatch Thread (EDT).

```java
1: package com.group11.courseregistration;
```
- **Line 1**: Defines package namespace matching Maven standard structure.

```java
3: import com.formdev.flatlaf.FlatLightLaf;
4: import com.group11.courseregistration.data.DataStore;
5: import com.group11.courseregistration.gui.LoginFrame;
7: import javax.swing.*;
```
- **Lines 3–7**: Imports FlatLaf for modern UI aesthetics, `DataStore` for initial data seeding, `LoginFrame` as the starting window, and core Swing classes.

```java
9: public class Main {
10:     public static void main(String[] args) {
11:         SwingUtilities.invokeLater(() -> {
```
- **Line 9**: Declares the public class `Main`.
- **Line 10**: JVM entry-point method `public static void main(String[] args)`.
- **Line 11**: `SwingUtilities.invokeLater(...)` places UI construction tasks on the **Event Dispatch Thread (EDT)**. In Swing, multi-threaded UI access causes race conditions and visual freezing; EDT guarantees single-threaded safety for GUI operations.

```java
13:             try {
14:                 UIManager.setLookAndFeel(new FlatLightLaf());
16:                 UIManager.put("defaultFont", new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
17:             } catch (Exception ex) {
18:                 System.err.println("Failed to initialize LaF");
19:             }
```
- **Lines 13–19**: Installs `FlatLightLaf` as the global Look & Feel, giving components soft border radii, modern padding, and clean typography. Overrides default font with 14pt `SansSerif`. Catches exceptions to ensure fallback if library initialization fails.

```java
22:             DataStore.initializeData();
```
- **Line 22**: Invokes static initializer on `DataStore` to pre-populate arrays, maps, and lists with sample students, courses, faculties, and enrollment records.

```java
25:             LoginFrame loginFrame = new LoginFrame();
26:             loginFrame.setVisible(true);
27:         });
28:     }
29: }
```
- **Lines 25–26**: Creates a new instance of `LoginFrame` and renders it visible on screen.

---

### File 2: `model/Course.java`
**Location**: `src/main/java/com/group11/courseregistration/model/Course.java`  
**Purpose**: Entity representing an academic course with seat capacity constraints and registration state.

```java
1: package com.group11.courseregistration.model;
3: public class Course {
4:     private String courseId;
5:     private String courseName;
6:     private String facultyName;
7:     private int credits;
8:     private int capacity;
9:     private int registeredStudents;
```
- **Lines 1–9**: Private attributes encapsulating course details:
  - `courseId`: Unique identifier (e.g. "CS101").
  - `courseName`: Descriptive course title.
  - `facultyName`: Assigned faculty instructor.
  - `credits`: Academic course weight (e.g. 3 or 4 credits).
  - `capacity`: Maximum allowed student enrollments.
  - `registeredStudents`: Current count of enrolled students.

```java
11:     public Course(String courseId, String courseName, String facultyName, int credits, int capacity) {
12:         this.courseId = courseId;
13:         this.courseName = courseName;
14:         this.facultyName = facultyName;
15:         this.credits = credits;
16:         this.capacity = capacity;
17:         this.registeredStudents = 0;
18:     }
```
- **Lines 11–18**: Constructor initializing all attributes. `registeredStudents` begins at `0` for newly created courses.

```java
20:     public String getCourseId() { return courseId; }
24:     public void setCourseId(String courseId) { this.courseId = courseId; }
28:     public String getCourseName() { return courseName; }
32:     public void setCourseName(String courseName) { this.courseName = courseName; }
36:     public String getFacultyName() { return facultyName; }
40:     public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
44:     public int getCredits() { return credits; }
48:     public void setCredits(int credits) { this.credits = credits; }
52:     public int getCapacity() { return capacity; }
56:     public void setCapacity(int capacity) { this.capacity = capacity; }
60:     public int getRegisteredStudents() { return registeredStudents; }
64:     public void setRegisteredStudents(int registeredStudents) { this.registeredStudents = registeredStudents; }
```
- **Lines 20–66**: Standard JavaBean getter and setter methods providing controlled access to private fields.

```java
68:     public int getAvailableSeats() {
69:         return capacity - registeredStudents;
70:     }
```
- **Lines 68–70**: Computed getter returning the remaining vacant seats.

```java
72:     public boolean isFull() {
73:         return registeredStudents >= capacity;
74:     }
```
- **Lines 72–74**: Capacity guard condition returning `true` if course has reached or exceeded max capacity.

```java
76:     public void registerStudent() {
77:         if (!isFull()) {
78:             registeredStudents++;
79:         }
80:     }
```
- **Lines 76–80**: Mutator incrementing enrolled student count only if the course is not full.

```java
82:     public void removeStudent() {
83:         if (registeredStudents > 0) {
84:             registeredStudents--;
85:         }
86:     }
```
- **Lines 82–86**: Mutator decrementing enrolled student count when a registration is cancelled, guarding against negative values.

---

### File 3: `model/Student.java`
**Location**: `src/main/java/com/group11/courseregistration/model/Student.java`  
**Purpose**: Entity representing an enrolled student and their login credentials.

```java
1: package com.group11.courseregistration.model;
3: public class Student {
4:     private String studentId;
5:     private String studentName;
6:     private String email;
7:     private String password;
8:     private String department;
9:     private int semester;
```
- **Lines 1–9**: Fields representing student identity, credentials (`email`, `password`), and academic cohort (`department`, `semester`).

```java
11:     public Student(String studentId, String studentName, String email, String password, String department, int semester) {
12:         this.studentId = studentId;
13:         this.studentName = studentName;
14:         this.email = email;
15:         this.password = password;
16:         this.department = department;
17:         this.semester = semester;
18:     }
```
- **Lines 11–18**: Full constructor initializing student profile.

```java
20:     public String getStudentId() { return studentId; }
24:     public void setStudentId(String studentId) { this.studentId = studentId; }
28:     public String getStudentName() { return studentName; }
32:     public void setStudentName(String studentName) { this.studentName = studentName; }
36:     public String getEmail() { return email; }
40:     public void setEmail(String email) { this.email = email; }
44:     public String getPassword() { return password; }
48:     public void setPassword(String password) { this.password = password; }
52:     public String getDepartment() { return department; }
56:     public void setDepartment(String department) { this.department = department; }
60:     public int getSemester() { return semester; }
64:     public void setSemester(int semester) { this.semester = semester; }
```
- **Lines 20–67**: Standard getters and setters.

---

### File 4: `model/Faculty.java`
**Location**: `src/main/java/com/group11/courseregistration/model/Faculty.java`  
**Purpose**: Entity representing academic instructors assigned to courses.

```java
1: package com.group11.courseregistration.model;
3: public class Faculty {
4:     private String facultyId;
5:     private String facultyName;
6:     private String department;
7:     private String email;
```
- **Lines 1–7**: Defines faculty properties (`facultyId`, `facultyName`, `department`, `email`).

```java
9:     public Faculty(String facultyId, String facultyName, String department, String email) {
10:         this.facultyId = facultyId;
11:         this.facultyName = facultyName;
12:         this.department = department;
13:         this.email = email;
14:     }
```
- **Lines 9–14**: Parameterized constructor.

```java
16:     public String getFacultyId() { return facultyId; }
20:     public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
24:     public String getFacultyName() { return facultyName; }
28:     public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
32:     public String getDepartment() { return department; }
36:     public void setDepartment(String department) { this.department = department; }
40:     public String getEmail() { return email; }
44:     public void setEmail(String email) { this.email = email; }
```
- **Lines 16–47**: Getters and setters for faculty properties.

---

### File 5: `model/Registration.java`
**Location**: `src/main/java/com/group11/courseregistration/model/Registration.java`  
**Purpose**: Junction entity recording enrollment transactions between Students and Courses.

```java
1: package com.group11.courseregistration.model;
3: import java.time.LocalDate;
5: public class Registration {
6:     private String registrationId;
7:     private String studentId;
8:     private String courseId;
9:     private LocalDate registrationDate;
10:     private String status;
```
- **Lines 1–10**: Fields linking a student (`studentId`) with an enrolled course (`courseId`), timestamped with `LocalDate`, and tracked via `status` ("REGISTERED" or "CANCELLED").

```java
12:     public Registration(String registrationId, String studentId, String courseId, LocalDate registrationDate, String status) {
13:         this.registrationId = registrationId;
14:         this.studentId = studentId;
15:         this.courseId = courseId;
16:         this.registrationDate = registrationDate;
17:         this.status = status;
18:     }
```
- **Lines 12–18**: Full constructor.

```java
20:     public String getRegistrationId() { return registrationId; }
24:     public void setRegistrationId(String registrationId) { this.registrationId = registrationId; }
28:     public String getStudentId() { return studentId; }
32:     public void setStudentId(String studentId) { this.studentId = studentId; }
36:     public String getCourseId() { return courseId; }
40:     public void setCourseId(String courseId) { this.courseId = courseId; }
44:     public LocalDate getRegistrationDate() { return registrationDate; }
48:     public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
52:     public String getStatus() { return status; }
56:     public void setStatus(String status) { this.status = status; }
```
- **Lines 20–59**: Getters and setters.

---

### File 6: `data/DataStore.java`
**Location**: `src/main/java/com/group11/courseregistration/data/DataStore.java`  
**Purpose**: Centralized in-memory storage holding the required data structures.

```java
1: package com.group11.courseregistration.data;
...
13: public class DataStore {
15:     // REQUIRED COLLECTIONS
16:     public static Course[] courses;
17:     public static LinkedList<Registration> registrations;
18:     public static HashMap<String, Student> students;
19:     public static HashMap<String, Course> courseMap;
20:     public static TreeMap<String, Course> sortedCourses;
22:     // Additional structure for faculties
23:     public static HashMap<String, Faculty> faculties;
```
- **Lines 16–23**: Declares the static data structures required by the project specifications:
  - `Course[] courses`: Fixed-size array for initial course storage (Objective 4).
  - `LinkedList<Registration> registrations`: Dynamic doubly-linked list preserving chronological order of all registrations (Objective 5).
  - `HashMap<String, Student> students`: Hash table offering average $O(1)$ constant time lookup for students by ID (Objective 6).
  - `HashMap<String, Course> courseMap`: Hash table offering $O(1)$ lookup for courses by ID (Objective 6).
  - `TreeMap<String, Course> sortedCourses`: Red-Black tree maintaining courses naturally sorted by `courseId` (Objective 7).
  - `HashMap<String, Faculty> faculties`: Hash table for faculty members.

```java
25:     public static void initializeData() {
26:         courses = new Course[6];
27:         registrations = new LinkedList<>();
28:         students = new HashMap<>();
29:         courseMap = new HashMap<>();
30:         sortedCourses = new TreeMap<>();
31:         faculties = new HashMap<>();
33:         loadSampleFaculties();
34:         loadSampleStudents();
35:         loadSampleCourses();
36:         loadSampleRegistrations();
37:     }
```
- **Lines 25–37**: Allocates memory for each collection and calls sample data population helper methods.

---

### File 7: `service/CourseService.java`
**Location**: `src/main/java/com/group11/courseregistration/service/CourseService.java`  
**Purpose**: Encapsulates course operations, maintaining consistency across `courseMap`, `sortedCourses`, and `courses[]`.

- **Lines 12–14 (`getAllCourses`)**: Returns all courses as an `ArrayList` from `DataStore.courseMap.values()`.
- **Lines 16–18 (`getSortedCourses`)**: Returns courses sorted by course ID directly from the `TreeMap`.
- **Lines 20–22 (`getCourse`)**: $O(1)$ constant-time lookup by ID using `HashMap`.
- **Lines 24–36 (`searchCourses`)**: Case-insensitive multi-attribute search scanning course ID, course title, and faculty name.
- **Lines 38–64 (`addCourse`)**:
  - Checks if `courseId` already exists (`DataStore.courseMap.containsKey(...)`), returning `false` on duplicate.
  - Inserts the course into the first null slot in `DataStore.courses[]`. If full, dynamically doubles array size using `System.arraycopy`, maintaining array integrity.
  - Puts the course into both `courseMap` and `sortedCourses`.
- **Lines 66–82 (`updateCourse`)**: Updates course details while ensuring the admin cannot set capacity lower than currently enrolled students.
- **Lines 84–95 (`deleteCourse`)**: Deletes course from `courseMap`, `sortedCourses`, and frees its slot in `courses[]`.

---

### File 8: `service/StudentService.java`
**Location**: `src/main/java/com/group11/courseregistration/service/StudentService.java`  
**Purpose**: Manages student authentication and directory queries.

- **Lines 11–17 (`login`)**: Validates student credentials against `DataStore.students`. Returns `Student` on match, `null` otherwise.
- **Lines 19–25**: Standard retrieval methods using `HashMap`.
- **Lines 27–39 (`searchStudents`)**: Multi-field student filter matching ID, name, or department.

---

### File 9: `service/FacultyService.java`
**Location**: `src/main/java/com/group11/courseregistration/service/FacultyService.java`  
**Purpose**: Manages faculty records, search operations, and new instructor additions.

- **Lines 10–12 (`getAllFaculties`)**: Returns a list of all instructors from `DataStore.faculties.values()`.
- **Lines 14–16 (`getFaculty`)**: $O(1)$ lookup for faculty details by `facultyId`.
- **Lines 18–31 (`searchFaculties`)**: Multi-field search matching ID, instructor name, department, or email.
- **Lines 33–39 (`addFaculty`)**: Adds a new faculty member with duplicate ID prevention.

---

### File 10: `service/RegistrationService.java`
**Location**: `src/main/java/com/group11/courseregistration/service/RegistrationService.java`  
**Purpose**: Enforces all business rules regarding registration, credit limits, duplicate prevention, and seat capacity.

- **Lines 13–17**: Defines `MAX_CREDITS = 18` and `RegistrationResult` enum (`SUCCESS`, `DUPLICATE`, `FULL`, `NOT_FOUND`, `CREDIT_LIMIT_EXCEEDED`).
- **Lines 19–39 (`registerStudent`)**:
  1. Validates that course exists; returns `NOT_FOUND` if null.
  2. Verifies no existing active registration; returns `DUPLICATE`.
  3. Checks `course.isFull()`; returns `FULL` if seats are exhausted.
  4. Checks semester credit cap (`getStudentTotalCredits(studentId) + course.getCredits() > MAX_CREDITS`); returns `CREDIT_LIMIT_EXCEEDED`.
  5. Calls `course.registerStudent()` to decrement available seats.
  6. Generates unique ID `R + (size + 100)` and appends a new `Registration` record to `DataStore.registrations`.
- **Lines 41–54 (`cancelRegistration`)**: Finds registration by ID, marks its status as `"CANCELLED"`, and calls `course.removeStudent()` to release the seat.
- **Lines 56–69 (`cancelRegistrationByStudentAndCourse`)**: Convenience method used by the Student UI to cancel enrollment by matching `studentId` and `courseId`.
- **Lines 71–78 (`isAlreadyRegistered`)**: Checks if the student is currently enrolled with `"REGISTERED"` status.
- **Lines 80–88 (`getStudentRegistrations`)**: Returns all active registrations for a given student.
- **Lines 90–92 (`getAllRegistrations`)**: Returns full historical list (including cancelled ones) for admin viewing.
- **Lines 94–104 (`getStudentTotalCredits`)**: Sums the credits of all active courses enrolled by the student.

---

### File 11: `utils/UIUtils.java`
**Location**: `src/main/java/com/group11/courseregistration/utils/UIUtils.java`  
**Purpose**: Centralizes the design system, color palette, fonts, and component factories.

- **Lines 13–27**: Defines cohesive color constants:
  - `COLOR_PRIMARY_BLUE`: `#2563EB` (Primary brand color).
  - `COLOR_DARK_NAVY`: `#0F172A` (Sidebar background).
  - `COLOR_DARKER_NAVY`: `#020617` (Hover state).
  - `COLOR_BACKGROUND`: `#F8FAFC` (App content background).
  - `COLOR_SUCCESS` / `COLOR_LIGHT_GREEN`: `#16A34A` / `#F0FDF4` (Badges).
  - `COLOR_DANGER` / `COLOR_LIGHT_RED`: `#DC2626` / `#FEF2F2` (Warnings/Buttons).
- **Lines 28–31**: Font hierarchy: Title (26pt Bold), Subtitle (16pt Plain), Normal (14pt Plain), Small (12pt Plain).
- **Lines 33–53**: Factory methods `createPrimaryButton` and `createDangerButton` applying custom backgrounds, padding, and hand cursor.
- **Lines 55–71**: Factory methods `createStyledTextField` and `createStyledPasswordField` applying custom line borders with internal padding.
- **Lines 73–79**: `createCard()` factory producing elevated white cards with light border outlines.
- **Lines 82–94**: `createTitleLabel` and `createSubtitleLabel` providing consistent heading typography.
- **Lines 96–116 (`styleTable`)**: Configures `JTable` with 44px rows, header styling (`#F1F5F9`), horizontal gridlines, light blue selection background, and padding renderers.

---

### File 12: `gui/LoginFrame.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/LoginFrame.java`  
**Purpose**: Dual-portal authentication window routing users to either the Student or Admin dashboard, with Enter key binding.

- **Lines 14–30**: Configures the 1100x700 window, centered on screen, split into Left (45%) and Right (55%) panels via `BorderLayout`.
- **Lines 32–78 (`createLeftPanel`)**: Dark Navy branding panel rendering the graduation cap emoji (`🎓`), system title, and slogans.
- **Lines 80–166 (`createRightPanel`)**: Centered white card containing:
  - Username and password fields.
  - "Sign In" button with authentication handler.
  - **Enter Key Binding**: `getRootPane().setDefaultButton(loginButton)` enables pressing <kbd>Enter</kbd> from either field to submit immediately.
  - Footnote displaying demo accounts for quick testing.

---

### File 13: `gui/StudentDashboard.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/StudentDashboard.java`  
**Purpose**: Main container for logged-in students with sidebar navigation, active highlight tracking, and automatic data synchronization.

- **Lines 23–56**: Constructor receiving authenticated `Student`. Manages 4 views via `CardLayout`:
  1. `"Dashboard"` $\rightarrow$ `StudentHomePanel`
  2. `"Available Courses"` $\rightarrow$ `CoursePanel`
  3. `"Register Course"` $\rightarrow$ `RegistrationPanel`
  4. `"My Courses"` $\rightarrow$ `MyCoursesPanel`
- **Lines 58–68 (`switchTab`)**: Centralized method to switch views, update sidebar highlight, and trigger live data reload on destination panels:
  - Calls `homePanel.refreshData()`, `coursePanel.loadData("")`, `registrationPanel.refreshPanel()`, or `myCoursesPanel.loadData()`.
- **Lines 70–78 (`updateNavSelection`)**: Paints the active navigation button with `UIUtils.COLOR_PRIMARY_BLUE` and resets others to `UIUtils.COLOR_DARK_NAVY`.
- **Lines 80–140**: Builds sidebar, logo, navigation buttons, and logout flow.
- **Lines 190–235**: Top header showing active view title and student profile pill.

---

### File 14: `gui/StudentHomePanel.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/StudentHomePanel.java`  
**Purpose**: Displays academic summary cards and live recent registration history.

- **Lines 30–38**: Personalized greeting banner.
- **Lines 40–58**: 4 metric cards: Total Courses, Available Courses, My Courses, and Total Credits.
- **Lines 69–85 (`refreshData`)**: Dynamically updates the 4 counter labels and rebuilds the recent courses container whenever the student navigates to Dashboard.
- **Lines 118–180 (`createRecentCoursesPanel`)**: Displays up to 3 most recently registered courses with green `"REGISTERED"` pill.

---

### File 15: `gui/CoursePanel.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/CoursePanel.java`  
**Purpose**: Searchable table displaying all courses and their capacity status.

- **Lines 45–56**: Header search field with "Search" button.
- **Lines 61–85**: Read-only `JTable` displaying Course ID, Name, Faculty, Credits, Capacity, Registered, Available, and Status.
- **Lines 89–112 (`loadData`)**: Public method querying `courseService` (sorted or filtered) and populating table rows.
- **Lines 114–135 (`StatusCellRenderer`)**: Custom cell renderer painting a green badge for "AVAILABLE" and red badge for "FULL".

---

### File 16: `gui/RegistrationPanel.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/RegistrationPanel.java`  
**Purpose**: Interactive interface allowing students to select a course, view details, and register.

- **Lines 55–59**: Course selection dropdown (`JComboBox`).
- **Lines 80–86 (`refreshPanel`)**: Public method reloading course items and info card.
- **Lines 88–94 (`loadCourseDropdown`)**: Fills dropdown with courses formatted as `"CS101 - Java Programming"`.
- **Lines 96–135 (`updateCourseInfo`)**: Dynamically updates the course info card. Disables "Confirm Registration" button if course is full.
- **Lines 155–175 (`attemptRegistration`)**: Invokes `registrationService.registerStudent(...)` and presents feedback: success dialog, duplicate registration alert, course full alert, or credit limit exceeded warning.

---

### File 17: `gui/MyCoursesPanel.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/MyCoursesPanel.java`  
**Purpose**: Lists student's enrolled courses with summary cards and one-click cancellation.

- **Lines 54–63**: Summary cards showing active registered course count and total enrolled credits.
- **Lines 68–95**: `JTable` showing enrolled courses with an interactive "Cancel" button in the final column.
- **Lines 117–141 (`loadData`)**: Public method fetching registrations and calculating total credits.
- **Lines 160–174 (`ButtonRenderer`)**: Paints the action cell as a red button labeled "Cancel".
- **Lines 176–228 (`ButtonEditor`)**: Custom cell editor handling button clicks:
  - Displays confirmation dialog (`JOptionPane.showConfirmDialog`).
  - Calls `registrationService.cancelRegistrationByStudentAndCourse(...)`.
  - Releases course seat and triggers table reload via `SwingUtilities.invokeLater`.

---

### File 18: `gui/AdminDashboard.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/AdminDashboard.java`  
**Purpose**: Main container for administrative tasks with sidebar navigation, active highlight tracking, and auto-refresh.

- **Lines 15–45**: Initializes `CardLayout` hosting 5 administrative views:
  1. `"Dashboard"` $\rightarrow$ `AdminHomePanel`
  2. `"Manage Courses"` $\rightarrow$ `AdminCoursePanel`
  3. `"Students"` $\rightarrow$ `AdminStudentPanel`
  4. `"Faculty"` $\rightarrow$ `AdminFacultyPanel`
  5. `"Registrations"` $\rightarrow$ `AdminRegistrationPanel`
- **Lines 47–65 (`switchTab`)**: Switches active panel, updates active nav highlight, and invokes `loadData()` / `refreshData()` on target panel.
- **Lines 67–75 (`updateNavSelection`)**: Manages active sidebar button coloring.
- **Lines 77–160**: Admin sidebar navigation and logout handling.

---

### File 19: `gui/AdminHomePanel.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/AdminHomePanel.java`  
**Purpose**: System dashboard providing administrative overview metrics and quick action shortcuts.

- **Lines 38–59**: 5 system overview metric cards:
  1. *Total Students*: Count of registered student accounts.
  2. *Total Courses*: Number of courses offered.
  3. *Total Faculty*: Number of active instructors.
  4. *Registrations*: Total registration transactions.
  5. *Open Seats*: Sum of remaining seats across all courses.
- **Lines 62–90**: Quick Action buttons navigating directly to "Manage Courses", "View Students", "Faculty Members", and "View Registrations".
- **Lines 94–110 (`refreshData`)**: Dynamically updates all 5 counters from backend services.

---

### File 20: `gui/AdminCoursePanel.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/AdminCoursePanel.java`  
**Purpose**: Course CRUD management panel with safe deletion guard.

- **Lines 45–48**: "+ Add Course" button launching modal creation form.
- **Lines 53–73**: Table displaying all courses and seat statistics.
- **Lines 79–107**: "Edit Selected" and "Delete Selected" buttons.
- **Safe Course Deletion**: Before allowing deletion, checks `course.getRegisteredStudents() > 0`. If active enrollments exist, displays a warning dialog preventing deletion until registrations are cancelled.
- **Lines 134–189 (`showAddCourseDialog`)**: Modal dialog validating input fields and adding to `courseService`.
- **Lines 191–251 (`showEditCourseDialog`)**: Modal dialog for editing existing courses, preventing capacity reduction below current enrolled student count.

---

### File 21: `gui/AdminStudentPanel.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/AdminStudentPanel.java`  
**Purpose**: Student directory interface.

- **Lines 41–52**: Search bar filtering students by ID, Name, or Department.
- **Lines 57–80**: Table listing Student ID, Name, Email, Department, and Semester.
- **Lines 82–101 (`loadData`)**: Public method populating student rows from `studentService`.

---

### File 22: `gui/AdminFacultyPanel.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/AdminFacultyPanel.java`  
**Purpose**: Faculty directory interface satisfying Problem Statement objective for managing faculty.

- **Lines 40–55**: Search bar and "+ Add Faculty" button.
- **Lines 59–85**: Table displaying Faculty ID, Instructor Name, Department, and Email Address.
- **Lines 87–105 (`loadData`)**: Public method querying `facultyService` to populate table rows.
- **Lines 107–158 (`showAddFacultyDialog`)**: Modal form to register new faculty members into `DataStore.faculties`.

---

### File 23: `gui/AdminRegistrationPanel.java`
**Location**: `src/main/java/com/group11/courseregistration/gui/AdminRegistrationPanel.java`  
**Purpose**: Audit log interface displaying all registration activities across the entire college.

- **Lines 51–53**: "Refresh" button reloading the log.
- **Lines 59–85**: Table displaying Registration ID, Student ID, Student Name, Course ID, Course Name, Registration Date, and Status badge.
- **Lines 87–110 (`loadData`)**: Public method reloading all records from `registrationService`.
- **Lines 112–132 (`StatusCellRenderer`)**: Paints green badge for `"REGISTERED"` and red badge for `"CANCELLED"`.

---

## 4. Academic Viva / Exam Questions & Concepts

### Q1: Why use `LinkedList` for registrations and `HashMap` for courses/students?
- **Answer**: `LinkedList` represents an event/audit log of registrations where entries are appended sequentially in $O(1)$ time and traversed chronologically. `HashMap` provides average $O(1)$ constant time lookup for students and courses by their unique alphanumeric IDs (`studentId`, `courseId`), which is critical for rapid search and authentication.

### Q2: What is the purpose of `TreeMap` in this system?
- **Answer**: `TreeMap` is backed by a self-balancing Red-Black binary search tree. It maintains its entries sorted naturally by key (`courseId`). This guarantees that courses are always displayed in alphabetical order in tables and dropdowns without having to re-sort a list repeatedly ($O(\log N)$ guarantee).

### Q3: Why is `SwingUtilities.invokeLater(...)` essential?
- **Answer**: Swing components are not thread-safe. All visual updates, dialog launches, and component mutations must run on the **Event Dispatch Thread (EDT)**. `invokeLater` posts tasks to the EDT's event queue, preventing thread contention, data corruption, and GUI lockups.

### Q4: How does the system handle real-time capacity monitoring?
- **Answer**: Each `Course` object maintains `capacity` and `registeredStudents`. When a student attempts registration:
  1. `RegistrationService` calls `course.isFull()`.
  2. If `registeredStudents < capacity`, `course.registerStudent()` increments the counter and a `Registration` record is saved.
  3. If `registeredStudents == capacity`, registration is rejected with a `FULL` status.
  4. Upon cancellation, `course.removeStudent()` decrements the counter, releasing the seat.
