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
