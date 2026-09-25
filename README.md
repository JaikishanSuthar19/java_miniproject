# College Course Registration Management System

**GROUP 11**

## PROBLEM STATEMENT
Modern college administration and student enrollment processes require an efficient, reliable, and user-friendly system to manage courses and registrations. This system digitizes the registration process, avoiding manual paperwork, conflicts, and over-enrollment.

## OBJECTIVES
- To provide a seamless, modern digital experience for student course registration.
- To allow administrators to effectively manage courses, students, and active registrations.
- To maintain data integrity, enforce course capacities, and handle real-time registration processing.

## FEATURES
- **Modern UI/UX**: Utilizing Java Swing with FlatLaf for a 2026-era modern SaaS application design.
- **Dual Portals**: Separate dashboards for Students and Administrators.
- **Course Management**: Admins can add, edit, and delete courses.
- **Real-time Capacity Enforcement**: Prevents students from registering for full courses.
- **Registration Management**: Students can view available courses, register, and cancel their registrations. Admins can monitor all registration activities.

## TECHNOLOGIES USED
- **Language**: Java 17
- **GUI Framework**: Java Swing & AWT
- **Look and Feel**: FlatLaf (FlatLightLaf)
- **Architecture**: Object-Oriented MVC-like Layered Architecture

## PACKAGE STRUCTURE
```
com.group11.courseregistration
├── data        # In-memory DataStore utilizing required data structures
├── gui         # Swing UI components (Dashboards, Panels, Frames)
├── model       # POJOs (Course, Student, Faculty, Registration)
├── service     # Business logic handling operations
└── utils       # UI styling, colors, component factories
```

## DATA STRUCTURES USED
As requested, the application heavily utilizes the Java Collections framework:
1. **Array (`Course[]`)**: Used in `DataStore` to store the fixed/initial set of courses.
2. **LinkedList (`LinkedList<Registration>`)**: Used in `DataStore` to maintain the chronological log of all course registrations.
3. **HashMap (`HashMap<String, Student>`, `HashMap<String, Course>`)**: Used for O(1) fast lookups of students and courses by their unique IDs.
4. **TreeMap (`TreeMap<String, Course>`)**: Used to automatically maintain courses sorted by their Course ID for display in tables and dropdowns.

## MODULES
- **Login**: Split-screen secure login routing to respective portals.
- **Student Dashboard**: 
  - **Home**: Overview statistics and recent courses.
  - **Available Courses**: Searchable table of courses with capacity and status.
  - **Register**: Selection dropdown with real-time capacity checks.
  - **My Courses**: Table of registered courses with one-click cancellation.
- **Admin Dashboard**:
  - **Home**: High-level system overview.
  - **Manage Courses**: Full CRUD operations for courses with capacity validation.
  - **Students**: Searchable directory of students.
  - **Registrations**: System-wide log of all course registrations and cancellations.

## TEAM MEMBERS & CONTRIBUTIONS (GROUP 11)

### 👨‍💻 Jaikishan
* **Java Coding**:
  - Developed `Student.java` (Model)
  - Developed `StudentService.java` (Business Logic)
  - Implemented student authentication and session credentials
  - Implemented student search and profile management
* **Swing Development**:
  - Developed `LoginFrame.java` (Split-screen authentication with Enter-key submission)
  - Developed `StudentDashboard.java` (Container with dynamic sidebar & active tab highlight)
  - Developed `StudentHomePanel.java` (Metric summary cards & recent registration history)
  - Implemented CardLayout navigation with automatic tab data synchronization
  - Integrated FlatLaf look-and-feel across student views
* **Main Contribution**: **Authentication System + Student Dashboard Architecture + Application Navigation & Live State Sync**

---

### 👨‍💻 Omkar
* **Java Coding**:
  - Developed `Course.java` (Model with seat capacity tracking)
  - Developed `CourseService.java` (CRUD business logic, array resizing, and map synchronization)
  - Implemented course capacity and availability checks
  - Implemented case-insensitive multi-attribute course search
* **Swing Development**:
  - Developed `CoursePanel.java` (Student course catalog with live search)
  - Developed `AdminCoursePanel.java` (Admin course management with modal dialogs)
  - Implemented custom JTable styling and status badge rendering (AVAILABLE / FULL)
  - Implemented modal Add Course and Edit Course dialogs with validation
  - Implemented **Safe Course Deletion Guard** (blocking deletion of courses with enrolled students)
* **Main Contribution**: **Course Management & Service + Course GUI + Safe CRUD Operations & Capacity Tracking**

---

### 👨‍💻 Sarthak
* **Java Coding**:
  - Developed `Registration.java` (Transaction Model)
  - Developed `RegistrationService.java` (Enrollment, cancellation, and validation rules)
  - Implemented duplicate registration prevention
  - Implemented real-time seat decrement and release logic
  - Implemented **Semester Maximum Credit Cap** (18 credits rule)
  - Implemented credit calculation per student
* **Swing Development**:
  - Developed `RegistrationPanel.java` (Course selection dropdown & dynamic info card)
  - Developed `MyCoursesPanel.java` (Enrolled courses table with interactive action buttons)
  - Built custom `TableCellRenderer` and `TableCellEditor` for the interactive "Cancel" button
  - Implemented credit cap warning dialogs and registration confirmation alerts
* **Main Contribution**: **Registration Service & Workflow + Credit Cap Validation + Cancellation & MyCourses UI**

---

### 👨‍💻 Danish
* **Java Coding**:
  - Developed `Faculty.java` (Model)
  - Developed `FacultyService.java` (Faculty directory and search operations)
  - Architected `DataStore.java` implementing the 4 required Java Collections (Array, LinkedList, HashMap, TreeMap)
  - Implemented student and faculty search algorithms
  - Handled data retrieval and collection synchronization
* **Swing Development**:
  - Developed `AdminDashboard.java` (Admin portal with active highlight & view synchronization)
  - Developed `AdminHomePanel.java` (System overview with 5 live metric cards & quick actions)
  - Developed `AdminStudentPanel.java` (Directory of students with live search)
  - Developed `AdminFacultyPanel.java` (Faculty directory with search & Add Faculty modal)
  - Developed `AdminRegistrationPanel.java` (College-wide registration audit log)
* **Main Contribution**: **Admin Dashboard & System Overview + Faculty Management System + DataStore Collections Architecture**

---

### 🤝 Shared Team Development
All four members actively collaborated on:
* Object-Oriented Architecture (MVC separation of Model, Data, Service, and GUI layers)
* Java Collections Framework integration & time complexity optimization ($O(1)$ HashMaps, $O(\log N)$ TreeMaps, Doubly-Linked Lists)
* Java Swing & AWT Event Dispatch Thread (EDT) safety
* Central Design System (`UIUtils.java`) with FlatLaf modern SaaS aesthetics
* Real-time cross-panel data synchronization and active tab state indicators
* Testing, debugging, edge-case validation, and comprehensive documentation (`CODE_EXPLANATION.md`)

---

## LOGIN CREDENTIALS
**Student**: `student` / `student123`
**Admin**: `admin` / `admin123`

## HOW TO RUN
A compilation and run script has been provided. Run the following in your terminal:
```bash
./compile.sh
./run.sh
```

## FUTURE SCOPE
- Integration with an SQL Database (MySQL/PostgreSQL) for persistent storage.
- Adding prerequisite checking before registration.
- Generating automated PDF schedules for students.
- Waitlist system for full courses.

## CONCLUSION
This mini-project successfully demonstrates the application of Object-Oriented Programming, Data Structures, and modern GUI design principles in Java, resulting in a professional-grade desktop application.
