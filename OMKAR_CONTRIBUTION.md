# 📚 Individual Project Report & Presentation Guide
## Member: Omkar
**Group 11 — College Course Registration Management System**  
**Role**: Lead Developer — Course Domain Model, Course Service & Course UI Management

---

## 📌 Executive Summary of Contribution

As part of Group 11, Omkar was responsible for the **Course Domain Model**, the **Course Service Layer & Data Structure Synchronization**, the **Student Course Catalog Interface**, and the **Administrative Course CRUD Operations**.

Key responsibilities included:
1. Designing the [Course](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/model/Course.java) entity with encapsulated capacity calculations (`getAvailableSeats()`, `isFull()`, `registerStudent()`, `removeStudent()`).
2. Building [CourseService](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/service/CourseService.java), managing data synchronization across the required collections: fixed array `Course[]`, `HashMap<String, Course>`, and `TreeMap<String, Course>`.
3. Implementing dynamic array resizing with `System.arraycopy` to honor the fixed array requirement while allowing safe additions.
4. Developing the student-side [CoursePanel](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/CoursePanel.java) with real-time multi-attribute search and custom status badge renderers.
5. Developing [AdminCoursePanel](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminCoursePanel.java) providing complete CRUD operations, modal input dialogs, and a **Safe Course Deletion Guard** that prevents deletion of courses with actively enrolled students.

---

## 📂 Assigned Files & Technical Ownership

| File | Layer | Key Responsibilities |
|---|---|---|
| [Course.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/model/Course.java) | Model | Encapsulates course code, title, instructor, credits, total capacity, and registered student count. |
| [CourseService.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/service/CourseService.java) | Service | Business logic for adding, editing, searching, and deleting courses; synchronizes Array, HashMap, and TreeMap. |
| [CoursePanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/CoursePanel.java) | GUI | Student-facing course catalog table, live search bar, and custom capacity badge rendering (`AVAILABLE` vs `FULL`). |
| [AdminCoursePanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/AdminCoursePanel.java) | GUI | Admin course management table with modal Add/Edit dialogs and safe deletion verification. |

---

## 💻 Detailed Technical Implementation

### 1. Course Capacity Rules (`Course.java`)
* Encapsulates seat calculations to ensure data integrity:
```java
// Course.java
public int getAvailableSeats() {
    return capacity - registeredStudents;
}

public boolean isFull() {
    return registeredStudents >= capacity;
}

public void registerStudent() {
    if (!isFull()) {
        registeredStudents++;
    }
}

public void removeStudent() {
    if (registeredStudents > 0) {
        registeredStudents--;
    }
}
```

### 2. Multi-Collection Synchronization & Dynamic Array Resizing (`CourseService.java`)
* Meets Problem Statement **Objective 4 (Arrays)**, **Objective 6 (HashMap)**, and **Objective 7 (TreeMap)** by synchronizing all three collections when a course is added or updated:
```java
public boolean addCourse(Course course) {
    if (DataStore.courseMap.containsKey(course.getCourseId())) {
        return false; // Prevent duplicate Course IDs
    }
    
    // Find empty slot in fixed array
    boolean addedToArray = false;
    for (int i = 0; i < DataStore.courses.length; i++) {
        if (DataStore.courses[i] == null) {
            DataStore.courses[i] = course;
            addedToArray = true;
            break;
        }
    }
    
    // If array is full, double its capacity dynamically
    if (!addedToArray) {
        Course[] newArray = new Course[DataStore.courses.length * 2];
        System.arraycopy(DataStore.courses, 0, newArray, 0, DataStore.courses.length);
        newArray[DataStore.courses.length] = course;
        DataStore.courses = newArray;
    }

    // Maintain O(1) Map and O(log N) Sorted TreeMap
    DataStore.courseMap.put(course.getCourseId(), course);
    DataStore.sortedCourses.put(course.getCourseId(), course);
    return true;
}
```

### 3. Safe Course Deletion Guard (`AdminCoursePanel.java`)
* Deleting a course with active registrations causes orphaned records in other tables. A safety guard was implemented to verify that no students are enrolled before allowing deletion:
```java
Course course = courseService.getCourse(courseId);
if (course != null && course.getRegisteredStudents() > 0) {
    JOptionPane.showMessageDialog(this,
        "Cannot delete course '" + course.getCourseName() + "'!\n" +
        course.getRegisteredStudents() + " student(s) are actively enrolled.\n" +
        "Registrations must be cancelled before deleting.",
        "Course Deletion Denied", JOptionPane.WARNING_MESSAGE);
    return;
}
```

### 4. Custom JTable Status Badge Renderer (`CoursePanel.java`)
* Extends `JLabel` and implements `TableCellRenderer` to display high-contrast status pills:
```java
class StatusCellRenderer extends JLabel implements TableCellRenderer {
    public StatusCellRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setFont(new Font("SansSerif", Font.BOLD, 11));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        String status = (String) value;
        setText(status);
        if ("AVAILABLE".equals(status)) {
            setBackground(UIUtils.COLOR_LIGHT_GREEN);
            setForeground(UIUtils.COLOR_SUCCESS);
        } else {
            setBackground(UIUtils.COLOR_LIGHT_RED);
            setForeground(UIUtils.COLOR_DANGER);
        }
        return this;
    }
}
```

---

## 🎤 Presentation & Viva Speaking Script

Use this script during your turn to present:

### 1. Introduction (30 seconds)
> *"Good morning/afternoon, teachers. My name is **Omkar**, and in Group 11's Course Registration System, my primary responsibility was developing the **Course Entity**, the **Course Service Layer**, and the **Course User Interfaces** for both students and administrators."*

### 2. Technical Walkthrough (1 - 2 minutes)
> *"In `Course.java`, I encapsulated the course attributes including `credits`, `capacity`, and `registeredStudents`. I implemented methods like `getAvailableSeats()` and `isFull()` so capacity rules are strictly enforced at the object level.*
>
> *In `CourseService.java`, I was responsible for integrating three required data structures: the fixed array `Course[]`, `HashMap<String, Course>`, and `TreeMap<String, Course>`. When a course is added or updated, my service keeps all three collections synchronized. To handle the fixed array requirement without crashing when new courses are added, I wrote dynamic resizing logic using `System.arraycopy`.*
>
> *On the UI side, I developed `CoursePanel.java` for students and `AdminCoursePanel.java` for administrators. In the student table, I built a custom `TableCellRenderer` that displays real-time green 'AVAILABLE' badges or red 'FULL' badges based on seat vacancy.*
>
> *In `AdminCoursePanel.java`, I built full CRUD functionality with modal dialogs for adding and editing courses. I also implemented a **Safe Deletion Guard**: if an admin attempts to delete a course that students are actively enrolled in, the system blocks the deletion with an explanatory alert, preserving registration data integrity."*

### 3. Live Demo Script (1 minute)
> *(While clicking on the running application)*
> 1. *"First, let's look at the Student Portal under 'Available Courses'.*
> 2. *Here is the course catalog. Notice that courses like `CS101` have available seats and display a green `AVAILABLE` badge, while `CS104` (Computer Networks) has 40/40 seats filled and displays a red `FULL` badge.*
> 3. *If I type 'Data' or 'Mehta' in the search bar, the table filters instantaneously across Course ID, Course Name, and Faculty.*
> 4. *Now let's switch to the Admin Portal under 'Manage Courses'.*
> 5. *Here I can click '+ Add Course' to launch a modal form with input validation.*
> 6. *If I select `CS104` and click 'Delete Selected', the Safe Deletion Guard activates and warns me that 40 students are enrolled, successfully blocking invalid deletion."*

---

## ❓ Probable Viva Questions & Model Answers

### Q1: Why do you maintain both a `HashMap` and a `TreeMap` for courses in `CourseService`?
**Answer**:
> *"They serve two distinct purposes with different time complexities. `HashMap<String, Course>` provides average $O(1)$ constant time lookup when retrieving a course by its unique Course ID. However, HashMaps do not maintain any order. `TreeMap<String, Course>` uses a self-balancing Red-Black binary search tree to automatically keep courses sorted in natural alphabetical order by Course ID in $O(\log N)$ time, which is ideal for populating our tables and dropdowns."*

### Q2: How did you implement fixed array storage for courses as required by the problem statement?
**Answer**:
> *"In `DataStore.java`, we initialize `public static Course[] courses = new Course[6]`. In `CourseService.java`, my `addCourse` method scans for an empty `null` slot in the array. If the initial array is full, it dynamically allocates a new array of double the size and uses `System.arraycopy` to preserve existing elements while appending the new course, combining the fixed array requirement with dynamic safety."*

### Q3: How does your custom `TableCellRenderer` work in the JTable?
**Answer**:
> *"Swing's `JTable` uses the Flyweight design pattern for rendering cells. Rather than creating thousands of separate components, `JTable` uses a single renderer component—in our case, a class extending `JLabel` implementing `TableCellRenderer`. In `getTableCellRendererComponent`, we check the cell's string value: if it is `'AVAILABLE'`, we set the background to light green with dark green text; if `'FULL'`, we set the background to light red with dark red text."*

### Q4: How does your edit course validation prevent invalid capacity changes?
**Answer**:
> *"In `CourseService.java`'s `updateCourse` method, we compare `updatedCourse.getCapacity()` against `existing.getRegisteredStudents()`. If an administrator tries to lower the course capacity below the number of students already enrolled, the method returns `false`, and the UI displays an error stating that capacity cannot be reduced below the active registration count."*

### Q5: What is the Safe Course Deletion Guard and why is it important?
**Answer**:
> *"Without this check, deleting a course that has enrolled students leaves dangling references in `DataStore.registrations`. When those students later view their registered courses, queries for the course ID would return `null`. The Safe Deletion Guard checks `course.getRegisteredStudents() > 0` and requires all registrations to be cancelled before a course can be removed."*
