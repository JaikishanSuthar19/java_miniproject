# Individual Project Report & Presentation Guide
## Member: Jaikishan
**Group 11 — College Course Registration Management System**  
**Role**: Lead Developer — Authentication, Student Domain & UI Navigation Architecture

---

## Executive Summary of Contribution

As part of Group 11, Jaikishan was responsible for the **User Authentication System**, the **Student Domain Model and Business Logic**, and the **Student Dashboard Shell & Navigation Architecture**. 

Key responsibilities included:
1. Architecting the entry point and credential verification for both Students and Administrators.
2. Building the [Student](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/model/Student.java) entity and [StudentService](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/service/StudentService.java) business logic.
3. Designing the split-screen modern SaaS login window ([LoginFrame](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/LoginFrame.java)) with FlatLaf styling and keyboard accessibility.
4. Implementing the student dashboard container ([StudentDashboard](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/StudentDashboard.java)) utilizing `CardLayout`, custom sidebar navigation, dynamic active tab highlighting, and cross-panel data synchronization (auto-refresh).
5. Designing the [StudentHomePanel](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/StudentHomePanel.java) displaying real-time metric cards and recent course registrations.

---

## Assigned Files & Technical Ownership

| File | Layer | Key Responsibilities |
|---|---|---|
| [Student.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/model/Student.java) | Model | Encapsulates student profile, ID, name, email, credentials, department, and semester. |
| [StudentService.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/service/StudentService.java) | Service | Authentication verification, $O(1)$ student lookup, multi-field search logic. |
| [LoginFrame.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/LoginFrame.java) | GUI | Dual-role login routing (Admin / Student), input fields, Enter-key submission binding. |
| [StudentDashboard.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/StudentDashboard.java) | GUI | Main student shell, `CardLayout` management, active tab visual state, auto-refresh triggers. |
| [StudentHomePanel.java](file:///Users/danishshaikh1423/Desktop/java-mini-ass/java_miniproject/src/main/java/com/group11/courseregistration/gui/StudentHomePanel.java) | GUI | Welcome greeting, 4 dynamic summary metric cards, recent registration audit display. |

---

## Detailed Technical Implementation

### 1. Authentication Logic (`StudentService.java` & `LoginFrame.java`)
* The login workflow is decoupled into GUI validation and Service authentication:
```java
// StudentService.java - O(1) hash lookup and password validation
public Student login(String username, String password) {
    Student student = DataStore.students.get(username);
    if (student != null && student.getPassword().equals(password)) {
        return student;
    }
    return null;
}
```
* **Enter-Key Accessibility**: In `LoginFrame.java`, the login button is bound as the default button on the window's `JRootPane`:
```java
// Allows pressing Enter anywhere in login window to submit
card.add(loginButton);
getRootPane().setDefaultButton(loginButton);
```

### 2. Tab Navigation & Live Data Auto-Refresh (`StudentDashboard.java`)
* Built with `CardLayout` to allow instantaneous switching between sub-panels without opening multiple windows.
* Whenever a student navigates between tabs, `switchTab(String cardName)` updates the sidebar visual highlight and triggers the target panel's refresh method:
```java
public void switchTab(String cardName) {
    cardLayout.show(cardPanel, cardName);
    headerTitle.setText(cardName);
    updateNavSelection(cardName);

    // Live state synchronization
    if ("Dashboard".equals(cardName)) {
        homePanel.refreshData();
    } else if ("Available Courses".equals(cardName)) {
        coursePanel.loadData("");
    } else if ("Register Course".equals(cardName)) {
        registrationPanel.refreshPanel();
    } else if ("My Courses".equals(cardName)) {
        myCoursesPanel.loadData();
    }
}
```

### 3. Active Nav Highlight State
* In the sidebar, the currently active tab is highlighted in royal blue (`#2563EB`) while inactive tabs remain dark navy (`#0F172A`).
```java
private void updateNavSelection(String activeTab) {
    for (Map.Entry<String, JLabel> entry : navButtons.entrySet()) {
        if (entry.getKey().equals(activeTab)) {
            entry.getValue().setBackground(UIUtils.COLOR_PRIMARY_BLUE);
        } else {
            entry.getValue().setBackground(UIUtils.COLOR_DARK_NAVY);
        }
    }
}
```

### 4. Dynamic Dashboard Overview (`StudentHomePanel.java`)
* Contains dynamic counters for **Total Courses**, **Available Courses**, **My Courses**, and **Total Credits**, backed by `refreshData()` to ensure calculations stay up to date when courses are added or cancelled.

---

## Presentation & Viva Speaking Script

Use this script during your turn to present:

### 1. Introduction (30 seconds)
> *"Good morning/afternoon, teachers. My name is **Jaikishan**, and in Group 11's College Course Registration System, my primary responsibility was architecting the **User Authentication System**, the **Student Domain & Service Layer**, and the overall **Student Dashboard Shell & Navigation Framework**."*

### 2. Technical Walkthrough (1 - 2 minutes)
> *"On the backend, I developed `Student.java` and `StudentService.java`. The student records are indexed in `DataStore.students` using a `HashMap<String, Student>`, which allows my authentication method to verify credentials in $O(1)$ constant time without scanning an entire list.*
>
> *On the frontend, I developed `LoginFrame.java`. I designed a modern split-screen interface using FlatLaf styling. It supports dual-portal authentication: entering admin credentials directs to the Admin Portal, while student credentials retrieve the student profile and launch `StudentDashboard.java`. I also bound the Enter key to the root pane so users can seamlessly sign in by hitting Enter.*
>
> *In `StudentDashboard.java`, I implemented a `CardLayout` navigation system. A common issue in Swing is stale data when switching tabs; to solve this, I designed a centralized `switchTab` method that updates the active sidebar highlight and automatically triggers a live reload on the target panel—for example, refreshing real-time registered credits and capacity counters on `StudentHomePanel`."*

### 3. Live Demo Script (1 minute)
> *(While clicking on the running application)*
> 1. *"Here is our `LoginFrame`. Notice the clean 2026-era SaaS styling and the demo credentials displayed at the bottom.*
> 2. *I will enter `student` and `student123` and press the **Enter key** on my keyboard.*
> 3. *The login window closes and opens the `StudentDashboard`.*
> 4. *In the top-right header, you can see the student's dynamic avatar pill, name 'Demo Student', department 'CSE', and semester.*
> 5. *On the Home panel, you can see live academic metrics: Total Courses (6), Available Courses (5), My Courses (3), and Total Credits (10).*
> 6. *When I click on other sidebar links like 'Available Courses' or 'My Courses', notice how the active tab turns bright blue, and the view updates instantly without lag."*

---

## ❓ Probable Viva Questions & Model Answers

### Q1: Why did you choose `HashMap` for student authentication in `StudentService` instead of an array or LinkedList?
**Answer**:
> *"An array or LinkedList requires linear search ($O(N)$ time complexity) because you have to iterate through every element until you find a matching username. By using `HashMap<String, Student>`, where the student ID is the key, Java computes the hash code and retrieves the record in average $O(1)$ constant time, making login instant even if there are thousands of students."*

### Q2: How does `CardLayout` work in your `StudentDashboard`? Why not create separate JFrames for each page?
**Answer**:
> *"Creating separate `JFrame` windows for every screen causes window clutter, visual flickering, and resource overhead. `CardLayout` treats each screen as a card in a deck within a single window. We can flip between `StudentHomePanel`, `CoursePanel`, `RegistrationPanel`, and `MyCoursesPanel` seamlessly while preserving the persistent header and sidebar."*

### Q3: How did you fix the stale data problem when switching tabs?
**Answer**:
> *"In Swing, panels created in constructors don't automatically know when they become visible again. In `StudentDashboard.java`, I created a `switchTab(String cardName)` method. When a user clicks a sidebar tab, this method not only calls `cardLayout.show(...)`, but also explicitly invokes the data-refresh method on the destination panel (e.g. `homePanel.refreshData()` or `myCoursesPanel.loadData()`), ensuring all counts and table records are instantly synchronized."*

### Q4: How is thread safety maintained when launching the login window in `Main.java`?
**Answer**:
> *"Swing components are not thread-safe. All UI creation and updates must be executed on the **Event Dispatch Thread (EDT)**. In `Main.java`, we invoke `SwingUtilities.invokeLater(() -> { ... })` to ensure the GUI is built safely on the EDT, preventing potential deadlocks and visual anomalies."*

### Q5: How did you implement keyboard Enter-key submission for the login form?
**Answer**:
> *"I used `getRootPane().setDefaultButton(loginButton);`. In Swing, the `JRootPane` listens for the default action key (Enter). Setting our sign-in button as the default button triggers its `ActionListener` automatically whenever Enter is pressed in either the username or password fields."*
