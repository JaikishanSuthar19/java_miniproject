# Individual Project Report & Presentation Guide
## Member: Sarthak
**Group 11 — College Course Registration Management System**  
**Role**: Lead Developer — Registration Domain, Business Rules Engine & Student Enrollment UI

---

## Executive Summary of Contribution

As part of Group 11, Sarthak was responsible for the core transactional engine of the application: the **Registration Domain Model**, the **Registration Service Layer & Validation Rules**, the **Course Enrollment Interface**, and the **My Courses Management Panel**.

Key responsibilities included:
1. Designing the [Registration](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/model/Registration.java) transaction model timestamped with `java.time.LocalDate`.
2. Building [RegistrationService](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/service/RegistrationService.java) utilizing a `LinkedList<Registration>` to maintain a chronological audit log of all registration activities.
3. Implementing core business constraints: duplicate registration detection, course capacity enforcement, and the **Semester Maximum Credit Cap** (18-credit limit rule).
4. Implementing the two-way cancellation workflow that updates transaction status to `"CANCELLED"` and immediately releases the seat back to the course.
5. Developing [RegistrationPanel](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/RegistrationPanel.java) with an interactive course selection dropdown and real-time detail card.
6. Developing [MyCoursesPanel](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/MyCoursesPanel.java) with total credit calculations, dynamic summary cards, and an interactive **"Cancel"** button embedded in table rows using custom Swing `TableCellEditor`.

---

## Assigned Files & Technical Ownership

| File | Layer | Key Responsibilities |
|---|---|---|
| [Registration.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/model/Registration.java) | Model | Junction entity linking Student and Course; tracks unique Registration ID, date, and status. |
| [RegistrationService.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/service/RegistrationService.java) | Service | Manages enrollment, duplicate checks, seat decrements, credit cap validation, and seat restoration on cancellation. |
| [RegistrationPanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/RegistrationPanel.java) | GUI | Interactive dropdown selector, dynamic course detail card, and registration confirmation handlers. |
| [MyCoursesPanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/MyCoursesPanel.java) | GUI | Displays registered courses, computes total credits, and embeds an interactive JTable row cancellation button. |

---

## Detailed Technical Implementation

### 1. Robust Registration Validation Engine (`RegistrationService.java`)
* Executes a 4-tier validation pipeline before approving any registration:
```java
public static final int MAX_CREDITS = 18;

public enum RegistrationResult {
    SUCCESS, DUPLICATE, FULL, NOT_FOUND, CREDIT_LIMIT_EXCEEDED
}

public RegistrationResult registerStudent(String studentId, String courseId) {
    Course course = DataStore.courseMap.get(courseId);
    if (course == null) return RegistrationResult.NOT_FOUND;

    // Check 1: Duplicate Registration
    if (isAlreadyRegistered(studentId, courseId)) {
        return RegistrationResult.DUPLICATE;
    }

    // Check 2: Seat Capacity
    if (course.isFull()) {
        return RegistrationResult.FULL;
    }

    // Check 3: Semester Credit Limit (Max 18 Credits)
    if (getStudentTotalCredits(studentId) + course.getCredits() > MAX_CREDITS) {
        return RegistrationResult.CREDIT_LIMIT_EXCEEDED;
    }

    // Decrement available seat count
    course.registerStudent();
    
    // Append to LinkedList audit log
    String regId = "R" + (DataStore.registrations.size() + 100);
    Registration reg = new Registration(regId, studentId, courseId, LocalDate.now(), "REGISTERED");
    DataStore.registrations.add(reg);
    
    return RegistrationResult.SUCCESS;
}
```

### 2. Immediate Seat Release on Cancellation (`RegistrationService.java`)
* Cancelling a registration preserves audit history by marking the record as `"CANCELLED"` while immediately releasing the seat back to the course:
```java
public boolean cancelRegistrationByStudentAndCourse(String studentId, String courseId) {
    for (Registration reg : DataStore.registrations) {
        if (reg.getStudentId().equals(studentId) && 
            reg.getCourseId().equals(courseId) && 
            reg.getStatus().equals("REGISTERED")) {
            
            reg.setStatus("CANCELLED");
            
            Course course = DataStore.courseMap.get(courseId);
            if (course != null) {
                course.removeStudent(); // Restores seat
            }
            return true;
        }
    }
    return false;
}
```

### 3. Interactive Table Button Renderer & Editor (`MyCoursesPanel.java`)
* To allow one-click cancellation directly from a table row, a custom `TableCellRenderer` and `DefaultCellEditor` were implemented:
```java
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private int currentRow;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton("Cancel");
        button.setBackground(UIUtils.COLOR_DANGER);
        button.setForeground(UIUtils.COLOR_WHITE);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Object getCellEditorValue() {
        String courseId = (String) table.getValueAt(currentRow, 0);
        String courseName = (String) table.getValueAt(currentRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(MyCoursesPanel.this,
            "Are you sure you want to cancel\n" + courseName + "?",
            "Cancel Course Registration?",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            registrationService.cancelRegistrationByStudentAndCourse(student.getStudentId(), courseId);
            SwingUtilities.invokeLater(() -> loadData()); // Safe EDT reload
        }
        return "Cancel";
    }
}
```

---

## Presentation & Viva Speaking Script

Use this script during your turn to present:

### 1. Introduction (30 seconds)
> *"Good morning/afternoon, teachers. My name is **Sarthak**, and in Group 11's Course Registration System, my primary responsibility was developing the **Course Registration Engine**, the **Academic Validation Rules**, and the **Student Enrollment & Cancellation User Interfaces**."*

### 2. Technical Walkthrough (1 - 2 minutes)
> *"In `Registration.java`, I designed the junction entity that connects a Student with an enrolled Course, timestamped with modern Java `LocalDate`.
>
> *In `RegistrationService.java`, I utilized a `LinkedList<Registration>` as required by Problem Statement Objective 5. A `LinkedList` is the ideal data structure here because it maintains a chronological audit trail where new registrations are appended in $O(1)$ time without resizing arrays.
>
> *My registration engine enforces four critical rules:
> 1. It verifies the course exists.
> 2. It prevents duplicate registrations by checking if the student is already enrolled.
> 3. It checks `course.isFull()` to stop over-enrollment.
> 4. It enforces a **Semester Maximum Credit Cap of 18 credits** to prevent student overload.
>
> *On the UI side, I developed `RegistrationPanel.java`, which features an interactive dropdown and dynamic card showing faculty, credits, and available seats. I also created `MyCoursesPanel.java`. In that panel, I implemented a custom Swing `TableCellEditor` and `TableCellRenderer`, embedding an active 'Cancel' button into every table row. When clicked, it confirms the cancellation, marks the record as CANCELLED, and immediately restores the seat in the course."*

### 3. Live Demo Script (1 minute)
> *(While clicking on the running application)*
> 1. *"Let's go to the 'Register Course' tab in the Student Portal.*
> 2. *Notice the dropdown. If I select `CS102 - Data Structures`, the info card instantly updates with faculty Prof. Patel, 4 credits, and 5 available seats.*
> 3. *If I select `CS104 - Computer Networks`, which has 0 available seats, the 'Confirm Registration' button automatically disables.*
> 4. *Now let's select `CS102` and click 'Confirm Registration'. We receive a success dialog: 'You are now registered for CS102'.*
> 5. *If I attempt to register for `CS102` again, the duplicate check triggers: 'You are already registered for this course.'*
> 6. *Now let's click 'My Courses'. `CS102` appears in the table, and our Total Credits counter increased.*
> 7. *If I click the red 'Cancel' button next to `CS102`, a confirmation dialog appears. Once confirmed, the course is removed and its seat is immediately released back to the course catalog."*

---

## ❓ Probable Viva Questions & Model Answers

### Q1: Why did you use `LinkedList` for registrations instead of `ArrayList` or `HashMap`?
**Answer**:
> *"Registrations represent an ongoing transactional event log. `LinkedList` provides $O(1)$ constant time appends at the tail without requiring contiguous memory reallocation, which is typical for audit trails. Furthermore, maintaining the chronological order of transactions is natural with a linked structure, fulfilling Problem Statement Objective 5."*

### Q2: What happens behind the scenes when a student cancels a course?
**Answer**:
> *"When cancellation is requested:
> 1. `RegistrationService` searches `DataStore.registrations` for an active record matching the `studentId` and `courseId`.
> 2. Instead of physically deleting the record, it updates the status from `'REGISTERED'` to `'CANCELLED'` to preserve historical audit data.
> 3. It fetches the `Course` from `courseMap` and calls `course.removeStudent()`, decrementing `registeredStudents` by 1 and instantly freeing up a seat.
> 4. `MyCoursesPanel.java` re-queries active registrations and updates the credit counter on the Event Dispatch Thread."*

### Q3: How did you implement the interactive "Cancel" button inside a JTable cell?
**Answer**:
> *"By default, a `JTable` cell only renders text. To make an interactive button, Swing requires two components:
> 1. A `TableCellRenderer` (`ButtonRenderer` extending `JButton`) that paints the appearance of a red button.
> 2. A `TableCellEditor` (`ButtonEditor` extending `DefaultCellEditor`) that intercepts mouse clicks, displays the confirmation dialog, and executes the cancellation logic when clicked."*

### Q4: How is the Semester Maximum Credit Limit enforced?
**Answer**:
> *"In `RegistrationService.java`, we defined `public static final int MAX_CREDITS = 18;`. When `registerStudent` is called, it sums the credits of the student's currently registered courses using `getStudentTotalCredits(studentId)` and adds the new course's credits. If `totalCredits + course.getCredits() > 18`, it returns `CREDIT_LIMIT_EXCEEDED`, and `RegistrationPanel` displays an alert explaining that the semester credit limit would be exceeded."*

### Q5: How do you prevent concurrent modification issues when deleting or updating table rows in Swing?
**Answer**:
> *"When a user clicks 'Cancel' in a table row, the table is currently in cell-editing mode. Modifying the table model directly while it is being edited causes Swing exceptions. To avoid this, we call `fireEditingStopped()` and schedule the table data reload using `SwingUtilities.invokeLater(() -> loadData())`. This defers the reload until after the cell editor has finished processing, ensuring thread safety."*
