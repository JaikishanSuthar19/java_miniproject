import os
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.dml.color import RGBColor
from pptx.enum.shapes import MSO_SHAPE

def build_presentation():
    prs = Presentation()
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)
    blank_layout = prs.slide_layouts[6]

    # Refined Human-Designed Dark Studio Palette
    BG_CANVAS      = RGBColor(9, 13, 22)       # #090D16 - Deep slate canvas
    CARD_BG        = RGBColor(19, 27, 42)      # #131B2A - Elevated card surface
    CARD_HOVER     = RGBColor(26, 36, 56)      # #1A2438
    CARD_BORDER    = RGBColor(37, 51, 71)      # #253347 - Crisp subtle border
    
    BRAND_INDIGO   = RGBColor(99, 102, 241)    # #6366F1 - Primary accent
    BRAND_LIGHT    = RGBColor(129, 140, 248)   # #818CF8 - High contrast tag
    ACCENT_CYAN    = RGBColor(56, 189, 248)    # #38BDF8 - Secondary accent
    ACCENT_EMERALD = RGBColor(16, 185, 129)    # #10B981 - Success & stability
    ACCENT_ROSE    = RGBColor(244, 63, 94)     # #F43F5E - Alert & delete guard
    ACCENT_AMBER   = RGBColor(245, 158, 11)    # #F59E0B - Warning
    
    TEXT_HEADING   = RGBColor(248, 250, 252)   # #F8FAFC - Crisp pure white
    TEXT_BODY      = RGBColor(203, 213, 225)   # #CBD5E1 - Readable bright silver
    TEXT_MUTED     = RGBColor(148, 163, 184)   # #94A3B8 - Secondary metadata
    
    TITLEBAR_BG    = RGBColor(22, 30, 46)      # #161E2E - macOS mockup titlebar
    
    FONT_DISPLAY   = "Trebuchet MS"
    FONT_BODY      = "Segoe UI"
    FONT_MONO      = "Courier New"

    def apply_slide_bg(slide):
        bg = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, prs.slide_width, prs.slide_height)
        bg.fill.solid()
        bg.fill.fore_color.rgb = BG_CANVAS
        bg.line.fill.background()
        return bg

    def add_slide_header(slide, title, category="COLLEGE COURSE REGISTRATION SYSTEM", subtitle=None):
        # Category Tag Pill
        cat_box = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(0.42), Inches(3.4), Inches(0.32))
        cat_box.fill.solid()
        cat_box.fill.fore_color.rgb = RGBColor(28, 38, 59)
        cat_box.line.color.rgb = BRAND_INDIGO
        cat_box.line.width = Pt(1)
        ctf = cat_box.text_frame
        ctf.word_wrap = False
        ctf.vertical_anchor = MSO_ANCHOR.MIDDLE
        cp = ctf.paragraphs[0]
        cp.text = category.upper()
        cp.font.name = FONT_DISPLAY
        cp.font.size = Pt(9.5)
        cp.font.bold = True
        cp.font.color.rgb = BRAND_LIGHT
        cp.alignment = PP_ALIGN.CENTER

        # Slide Title & Subtitle text box
        h_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.78), Inches(11.733), Inches(0.8))
        htf = h_box.text_frame
        htf.word_wrap = True
        htf.margin_left = 0
        htf.margin_top = 0
        
        tp = htf.paragraphs[0]
        tp.text = title
        tp.font.name = FONT_DISPLAY
        tp.font.size = Pt(21)
        tp.font.bold = True
        tp.font.color.rgb = TEXT_HEADING

        if subtitle:
            sp = htf.add_paragraph()
            sp.text = subtitle
            sp.font.name = FONT_BODY
            sp.font.size = Pt(11)
            sp.font.color.rgb = TEXT_MUTED
            sp.space_before = Pt(3)

    def draw_window_mockup(slide, x, y, width, height, title_text, img_path):
        # Window Mockup Outer Frame
        outer = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, x, y, width, height)
        outer.fill.solid()
        outer.fill.fore_color.rgb = RGBColor(12, 16, 26)
        outer.line.color.rgb = CARD_BORDER
        outer.line.width = Pt(1.2)

        # Title bar
        tb_height = Inches(0.38)
        tb = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, x, y, width, tb_height)
        tb.fill.solid()
        tb.fill.fore_color.rgb = TITLEBAR_BG
        tb.line.color.rgb = CARD_BORDER
        tb.line.width = Pt(1)

        # 3 Window Traffic Light Dots
        dot_y = y + Inches(0.12)
        dot_size = Inches(0.12)
        
        # Red
        dot_r = slide.shapes.add_shape(MSO_SHAPE.OVAL, x + Inches(0.18), dot_y, dot_size, dot_size)
        dot_r.fill.solid()
        dot_r.fill.fore_color.rgb = RGBColor(239, 68, 68)
        dot_r.line.fill.background()

        # Yellow
        dot_y_el = slide.shapes.add_shape(MSO_SHAPE.OVAL, x + Inches(0.36), dot_y, dot_size, dot_size)
        dot_y_el.fill.solid()
        dot_y_el.fill.fore_color.rgb = RGBColor(245, 158, 11)
        dot_y_el.line.fill.background()

        # Green
        dot_g = slide.shapes.add_shape(MSO_SHAPE.OVAL, x + Inches(0.54), dot_y, dot_size, dot_size)
        dot_g.fill.solid()
        dot_g.fill.fore_color.rgb = RGBColor(16, 185, 129)
        dot_g.line.fill.background()

        # Window Title in center of titlebar
        w_title_box = slide.shapes.add_textbox(x + Inches(0.8), y, width - Inches(1.6), tb_height)
        wtf = w_title_box.text_frame
        wtf.word_wrap = False
        wtf.vertical_anchor = MSO_ANCHOR.MIDDLE
        wp = wtf.paragraphs[0]
        wp.text = title_text
        wp.font.name = FONT_MONO
        wp.font.size = Pt(9.5)
        wp.font.color.rgb = TEXT_MUTED
        wp.alignment = PP_ALIGN.CENTER

        # Image placement inside frame
        if os.path.exists(img_path):
            img_x = x + Inches(0.08)
            img_y = y + tb_height + Inches(0.04)
            img_w = width - Inches(0.16)
            img_h = height - tb_height - Inches(0.12)
            slide.shapes.add_picture(img_path, img_x, img_y, img_w, img_h)

    # ==========================================
    # SLIDE 1: Title Slide (Developer Hero Layout)
    # ==========================================
    s1 = prs.slides.add_slide(blank_layout)
    apply_slide_bg(s1)

    # Hero Eyebrow Pill
    pill1 = s1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(0.7), Inches(3.8), Inches(0.36))
    pill1.fill.solid()
    pill1.fill.fore_color.rgb = RGBColor(28, 38, 59)
    pill1.line.color.rgb = BRAND_INDIGO
    pill1.line.width = Pt(1.2)
    ptf = pill1.text_frame
    ptf.vertical_anchor = MSO_ANCHOR.MIDDLE
    p = ptf.paragraphs[0]
    p.text = "MINI PROJECT #11 • JAVA PROGRAMMING"
    p.font.name = FONT_DISPLAY
    p.font.size = Pt(10)
    p.font.bold = True
    p.font.color.rgb = BRAND_LIGHT
    p.alignment = PP_ALIGN.CENTER

    # Main Big Title
    tb_title = s1.shapes.add_textbox(Inches(0.8), Inches(1.15), Inches(11.733), Inches(1.9))
    ttf = tb_title.text_frame
    ttf.word_wrap = True
    ttf.margin_left = 0
    ttf.margin_top = 0

    p_main = ttf.paragraphs[0]
    p_main.text = "College Course Registration"
    p_main.font.name = FONT_DISPLAY
    p_main.font.size = Pt(36)
    p_main.font.bold = True
    p_main.font.color.rgb = TEXT_HEADING

    p_sub = ttf.add_paragraph()
    p_sub.text = "Management System"
    p_sub.font.name = FONT_DISPLAY
    p_sub.font.size = Pt(36)
    p_sub.font.bold = True
    p_sub.font.color.rgb = BRAND_LIGHT
    p_sub.space_before = Pt(2)

    p_desc = ttf.add_paragraph()
    p_desc.text = "An object-oriented desktop application featuring dual-role access, real-time capacity monitoring, and rigorous multi-collection synchronization in Java."
    p_desc.font.name = FONT_BODY
    p_desc.font.size = Pt(13)
    p_desc.font.color.rgb = TEXT_BODY
    p_desc.space_before = Pt(10)

    # 4-Cell Academic Metadata Strip
    meta_box = s1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(3.3), Inches(11.733), Inches(1.05))
    meta_box.fill.solid()
    meta_box.fill.fore_color.rgb = CARD_BG
    meta_box.line.color.rgb = CARD_BORDER
    meta_box.line.width = Pt(1)

    meta_items = [
        ("SUBJECT", "Java Programming"),
        ("COHORT", "Jeff Bezos"),
        ("BATCH", "2025 – 2029"),
        ("PROBLEM STATEMENT", "Mini Project #11")
    ]
    for idx, (label, val) in enumerate(meta_items):
        cell_x = Inches(0.8 + idx * 2.93)
        cell_tb = s1.shapes.add_textbox(cell_x, Inches(3.38), Inches(2.9), Inches(0.9))
        ctf = cell_tb.text_frame
        ctf.word_wrap = True
        ctf.margin_left = Inches(0.2)
        ctf.margin_top = Inches(0.08)
        
        p_lbl = ctf.paragraphs[0]
        p_lbl.text = label
        p_lbl.font.name = FONT_DISPLAY
        p_lbl.font.size = Pt(9.5)
        p_lbl.font.bold = True
        p_lbl.font.color.rgb = TEXT_MUTED
        
        p_v = ctf.add_paragraph()
        p_v.text = val
        p_v.font.name = FONT_DISPLAY
        p_v.font.size = Pt(14)
        p_v.font.bold = True
        p_v.font.color.rgb = TEXT_HEADING
        p_v.space_before = Pt(3)

    # Team Members Header Label
    tb_team_hdr = s1.shapes.add_textbox(Inches(0.8), Inches(4.55), Inches(11.733), Inches(0.3))
    thtf = tb_team_hdr.text_frame
    thtf.margin_left = 0
    thp = thtf.paragraphs[0]
    thp.text = "ENGINEERING TEAM ROSTER (GROUP 11):"
    thp.font.name = FONT_DISPLAY
    thp.font.size = Pt(10)
    thp.font.bold = True
    thp.font.color.rgb = TEXT_MUTED

    # 4 Team Member Roster Cards
    members = [
        ("Jaikishan", "STUDENT & AUTH LEAD", "Authentication, Student Entity, Dashboard & Navigation"),
        ("Omkar", "COURSE DOMAIN LEAD", "Course Service, Dynamic Resizing & Safe Deletion Guard"),
        ("Sarthak", "REGISTRATION ENGINE", "Enrollment Rules, 18-Credit Cap & Table Button Editor"),
        ("Danish", "DATA & ADMIN LEAD", "Collections Architecture, Faculty System & Overview GUI")
    ]

    for idx, (name, role, summary) in enumerate(members):
        mx = Inches(0.8 + idx * 2.98)
        m_card = s1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, mx, Inches(4.9), Inches(2.78), Inches(2.0))
        m_card.fill.solid()
        m_card.fill.fore_color.rgb = CARD_BG
        m_card.line.color.rgb = CARD_BORDER
        m_card.line.width = Pt(1)

        mtf = m_card.text_frame
        mtf.word_wrap = True
        mtf.margin_left = Inches(0.2)
        mtf.margin_right = Inches(0.2)
        mtf.margin_top = Inches(0.2)

        p_name = mtf.paragraphs[0]
        p_name.text = name
        p_name.font.name = FONT_DISPLAY
        p_name.font.size = Pt(15)
        p_name.font.bold = True
        p_name.font.color.rgb = TEXT_HEADING

        p_role = mtf.add_paragraph()
        p_role.text = role
        p_role.font.name = FONT_DISPLAY
        p_role.font.size = Pt(9)
        p_role.font.bold = True
        p_role.font.color.rgb = BRAND_LIGHT
        p_role.space_before = Pt(3)

        p_sum = mtf.add_paragraph()
        p_sum.text = summary
        p_sum.font.name = FONT_BODY
        p_sum.font.size = Pt(10)
        p_sum.font.color.rgb = TEXT_MUTED
        p_sum.space_before = Pt(6)

    # ==========================================
    # SLIDE 2: Problem Statement & Motivation
    # ==========================================
    s2 = prs.slides.add_slide(blank_layout)
    apply_slide_bg(s2)
    add_slide_header(s2, "Problem Statement & Real-World Motivation", "CONTEXT • PROBLEM STATEMENT 11", "Modernizing university course enrollment workflows and preventing resource contention.")

    # Left: Legacy Friction Card
    c1 = s2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(1.8), Inches(5.6), Inches(5.1))
    c1.fill.solid()
    c1.fill.fore_color.rgb = CARD_BG
    c1.line.color.rgb = CARD_BORDER
    c1.line.width = Pt(1)

    tf1 = c1.text_frame
    tf1.word_wrap = True
    tf1.margin_left = Inches(0.35)
    tf1.margin_right = Inches(0.35)
    tf1.margin_top = Inches(0.35)

    p = tf1.paragraphs[0]
    p.text = "[ LEGACY FRICTION ]"
    p.font.name = FONT_DISPLAY
    p.font.size = Pt(9.5)
    p.font.bold = True
    p.font.color.rgb = ACCENT_ROSE

    p_h = tf1.add_paragraph()
    p_h.text = "Traditional Registration Pain Points"
    p_h.font.name = FONT_DISPLAY
    p_h.font.size = Pt(16)
    p_h.font.bold = True
    p_h.font.color.rgb = TEXT_HEADING
    p_h.space_before = Pt(4)

    pains = [
        ("Uncontrolled Over-Enrollment", "Without seat validation, popular classes fill past physical classroom capacity."),
        ("Duplicate Registrations", "Students enroll into duplicate sections, hoarding seats from peers."),
        ("Disconnected Faculty Records", "Instructor assignments and departments maintained in unlinked silos."),
        ("Semester Credit Overload", "Students enroll into excessive courses without prerequisite or credit caps.")
    ]
    for title, desc in pains:
        p_item = tf1.add_paragraph()
        p_item.text = f"•  {title}: {desc}"
        p_item.font.name = FONT_BODY
        p_item.font.size = Pt(11)
        p_item.font.color.rgb = TEXT_BODY
        p_item.space_before = Pt(8)

    # Right: Engineered Architecture Card
    c2 = s2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(6.8), Inches(1.8), Inches(5.733), Inches(5.1))
    c2.fill.solid()
    c2.fill.fore_color.rgb = CARD_BG
    c2.line.color.rgb = CARD_BORDER
    c2.line.width = Pt(1)

    tf2 = c2.text_frame
    tf2.word_wrap = True
    tf2.margin_left = Inches(0.35)
    tf2.margin_right = Inches(0.35)
    tf2.margin_top = Inches(0.35)

    p = tf2.paragraphs[0]
    p.text = "[ OUR ARCHITECTURE ]"
    p.font.name = FONT_DISPLAY
    p.font.size = Pt(9.5)
    p.font.bold = True
    p.font.color.rgb = ACCENT_EMERALD

    p_h2 = tf2.add_paragraph()
    p_h2.text = "The Engineered Software Solution"
    p_h2.font.name = FONT_DISPLAY
    p_h2.font.size = Pt(16)
    p_h2.font.bold = True
    p_h2.font.color.rgb = TEXT_HEADING
    p_h2.space_before = Pt(4)

    solves = [
        ("Real-Time Capacity Enforcement", "Capacity vs. registered count checked synchronously before confirming seat."),
        ("18-Credit Maximum Cap", "Hard rule stops students from exceeding university semester credit limits."),
        ("Faculty Management System", "Dedicated Faculty model, search filtering, and professor assignment directory."),
        ("Audit Trail Preservation", "Course cancellations preserve chronological LinkedList transaction history.")
    ]
    for title, desc in solves:
        p_item = tf2.add_paragraph()
        p_item.text = f"✓  {title}: {desc}"
        p_item.font.name = FONT_BODY
        p_item.font.size = Pt(11)
        p_item.font.color.rgb = TEXT_BODY
        p_item.space_before = Pt(8)

    # ==========================================
    # SLIDE 3: Core Objectives & Syllabus Alignment
    # ==========================================
    s3 = prs.slides.add_slide(blank_layout)
    apply_slide_bg(s3)
    add_slide_header(s3, "Core Objectives & Syllabus Alignment", "COMPLIANCE • SYLLABUS REQUIREMENTS", "Structured mapping to the 8 core objectives specified in Mini Project 11 guidelines.")

    objectives = [
        ("1. Entity Management", "Models for Student, Course, Faculty, and Registration with strict data encapsulation."),
        ("2. Registration Engine", "Real-time enrollment, cancellation, duplicate prevention, and credit calculation logic."),
        ("3. Capacity Monitoring", "Automatic vacancy calculation with visual AVAILABLE and FULL status indicators."),
        ("4. Fixed Array Storage", "Course[] array for initial course offerings with dynamic reallocation safety."),
        ("5. LinkedList Audit Log", "LinkedList<Registration> maintaining sequential event history for institutional auditing."),
        ("6. HashMap Fast Lookup", "O(1) constant-time record retrieval for Students, Courses, and Faculty by unique ID."),
        ("7. TreeMap Self-Sorting", "Self-balancing Red-Black Tree maintaining courses naturally sorted by Course ID."),
        ("8. Modern Swing GUI", "Desktop experience styled with FlatLaf, responsive CardLayout, and live state sync.")
    ]

    for idx, (title, desc) in enumerate(objectives):
        col = idx % 4
        row = idx // 4
        ox = Inches(0.8 + col * 2.98)
        oy = Inches(1.8 + row * 2.6)

        ocard = s3.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, ox, oy, Inches(2.78), Inches(2.35))
        ocard.fill.solid()
        ocard.fill.fore_color.rgb = CARD_BG
        ocard.line.color.rgb = CARD_BORDER
        ocard.line.width = Pt(1)

        otf = ocard.text_frame
        otf.word_wrap = True
        otf.margin_left = Inches(0.2)
        otf.margin_right = Inches(0.2)
        otf.margin_top = Inches(0.2)

        op_title = otf.paragraphs[0]
        op_title.text = title
        op_title.font.name = FONT_DISPLAY
        op_title.font.size = Pt(13)
        op_title.font.bold = True
        op_title.font.color.rgb = TEXT_HEADING

        op_desc = otf.add_paragraph()
        op_desc.text = desc
        op_desc.font.name = FONT_BODY
        op_desc.font.size = Pt(10.5)
        op_desc.font.color.rgb = TEXT_BODY
        op_desc.space_before = Pt(6)

    # ==========================================
    # SLIDE 4: Java Collections Deep Dive
    # ==========================================
    s4 = prs.slides.add_slide(blank_layout)
    apply_slide_bg(s4)
    add_slide_header(s4, "Java Collections Framework Deep Dive", "ALGORITHMS • COLLECTIONS ARCHITECTURE", "Technical comparison and algorithmic justification of chosen data structures.")

    ds_cards = [
        ("Course[] (Array)", "FIXED INITIAL STORAGE", "O(1) Index Access", "Objective 4 requirement. Stores predefined catalog. System.arraycopy dynamically doubles capacity when expanded.", BRAND_LIGHT),
        ("LinkedList<Registration>", "CHRONOLOGICAL AUDIT LOG", "O(1) Append", "Objective 5 requirement. Sequential event ledger preserving insertion order of active and cancelled records.", ACCENT_CYAN),
        ("HashMap<String, T>", "CONSTANT-TIME LOOKUP", "O(1) Avg Search", "Objective 6 requirement. Direct primary-key lookups for Students, Courses, and Faculty by unique ID string.", ACCENT_EMERALD),
        ("TreeMap<String, Course>", "SELF-SORTING CATALOG", "O(log N) Search", "Objective 7 requirement. Backed by Red-Black Binary Search Tree, keeping courses naturally alphabetized.", ACCENT_AMBER)
    ]

    for idx, (title, role, complexity, rationale, accent_col) in enumerate(ds_cards):
        dx = Inches(0.8 + idx * 2.98)
        dcard = s4.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, dx, Inches(1.8), Inches(2.78), Inches(5.1))
        dcard.fill.solid()
        dcard.fill.fore_color.rgb = CARD_BG
        dcard.line.color.rgb = CARD_BORDER
        dcard.line.width = Pt(1)

        dtf = dcard.text_frame
        dtf.word_wrap = True
        dtf.margin_left = Inches(0.25)
        dtf.margin_right = Inches(0.25)
        dtf.margin_top = Inches(0.3)

        dp = dtf.paragraphs[0]
        dp.text = f"[ {role} ]"
        dp.font.name = FONT_DISPLAY
        dp.font.size = Pt(8.5)
        dp.font.bold = True
        dp.font.color.rgb = accent_col

        dp_title = dtf.add_paragraph()
        dp_title.text = title
        dp_title.font.name = FONT_DISPLAY
        dp_title.font.size = Pt(14)
        dp_title.font.bold = True
        dp_title.font.color.rgb = TEXT_HEADING
        dp_title.space_before = Pt(4)

        dp_comp = dtf.add_paragraph()
        dp_comp.text = f"Complexity: {complexity}"
        dp_comp.font.name = FONT_MONO
        dp_comp.font.size = Pt(10.5)
        dp_comp.font.bold = True
        dp_comp.font.color.rgb = accent_col
        dp_comp.space_before = Pt(12)

        dp_why = dtf.add_paragraph()
        dp_why.text = f"\nArchitectural Justification:\n{rationale}"
        dp_why.font.name = FONT_BODY
        dp_why.font.size = Pt(11)
        dp_why.font.color.rgb = TEXT_BODY
        dp_why.space_before = Pt(6)

    # =========================================================================
    # SLIDES 5 TO 13: SCREENSHOT SHOWCASE SLIDES (Handcrafted macOS Mockups)
    # =========================================================================
    showcases = [
        (
            5,
            "Dual-Role Authentication Portal",
            "TOUR • 01 • AUTHENTICATION",
            "Split-screen modern login routing to Student or Administrator portals.",
            "screenshots/01_login_frame.png",
            "LoginFrame.java — Java Swing",
            "Authentication Highlights",
            [
                ("Dual-Portal Routing", "Entering 'admin' opens the Admin Dashboard; student credentials load the Student Portal."),
                ("Enter-Key Binding", "Bound via JRootPane.setDefaultButton() to submit immediately on pressing Enter."),
                ("Modern SaaS Aesthetic", "Dark navy branding panel paired with clean, rounded input fields."),
                ("Evaluator Demo Notice", "Clearly displays demo credentials at the bottom for frictionless academic testing.")
            ]
        ),
        (
            6,
            "Student Dashboard & Academic Overview",
            "TOUR • 02 • STUDENT OVERVIEW",
            "Real-time metric telemetry and recently enrolled course overview.",
            "screenshots/02_student_dashboard.png",
            "StudentDashboard.java — Overview Panel",
            "Dashboard Highlights",
            [
                ("4 Live Statistic Cards", "Total Courses, Available Courses, Enrolled Courses, and Total Enrolled Credits."),
                ("Recent Courses Audit", "Displays up to 3 most recently enrolled courses with credits and faculty names."),
                ("Active Tab Highlight", "Sidebar tab highlights royal blue (#2563EB) for clear visual state indication."),
                ("Dynamic Profile Pill", "Avatar badge reflects student initials, full name, department, and semester.")
            ]
        ),
        (
            7,
            "Course Catalog & Capacity Enforcement",
            "TOUR • 03 • COURSE DISCOVERY",
            "Interactive course catalog powered by TreeMap self-sorting and live search.",
            "screenshots/03_student_available_courses.png",
            "CoursePanel.java — Available Courses",
            "Catalog Highlights",
            [
                ("Real-Time Capacity", "Computes Available Seats = Capacity - Registered Students dynamically."),
                ("High-Contrast Pills", "Green 'AVAILABLE' badge vs. Red 'FULL' badge (e.g. CS104 40/40 filled)."),
                ("Live Search Bar", "Instant multi-field filtering across Course ID, Course Name, and Faculty."),
                ("TreeMap Backing", "Courses remain automatically alphabetized by Course ID across all views.")
            ]
        ),
        (
            8,
            "Course Registration & Academic Credit Cap",
            "TOUR • 04 • ENROLLMENT ENGINE",
            "Real-time enrollment engine enforcing capacity bounds and credit limits.",
            "screenshots/04_student_register_course.png",
            "RegistrationPanel.java — Course Enrollment",
            "Enrollment Highlights",
            [
                ("Dynamic Info Card", "Selecting course from combo box updates credits, instructor, and vacant seats."),
                ("Over-Enrollment Guard", "Register button auto-disables with alert if course capacity is exhausted."),
                ("Duplicate Prevention", "Blocks duplicate enrollment attempts with an explanatory warning dialog."),
                ("18-Credit Maximum Cap", "Hard stop prevents exceeding the semester limit (Syllabus safety enhancement).")
            ]
        ),
        (
            9,
            "My Courses & Interactive Cancellation",
            "TOUR • 05 • SCHEDULE MANAGEMENT",
            "Interactive schedule viewer with in-table cancellation action buttons.",
            "screenshots/05_student_my_courses.png",
            "MyCoursesPanel.java — Enrolled Courses",
            "Schedule Highlights",
            [
                ("Enrolled Schedule", "Detailed view of enrolled courses with credit breakdowns and registration dates."),
                ("Total Credits Meter", "Live counter displaying accumulated registered credits."),
                ("Table ButtonEditor", "Custom TableCellEditor renders functional red 'Cancel' action buttons inside table rows."),
                ("Instant Seat Release", "Cancelling dynamically restores course seat vacancy and updates all dashboards.")
            ]
        ),
        (
            10,
            "Admin Dashboard & Institutional Metrics",
            "TOUR • 06 • ADMINISTRATIVE PORTAL",
            "High-level institutional telemetry and real-time administrative counters.",
            "screenshots/06_admin_dashboard.png",
            "AdminDashboard.java — Overview Panel",
            "Admin Overview Highlights",
            [
                ("5 Institutional Metrics", "Total Students, Total Courses, Total Faculty, Total Registrations, and Open Seats."),
                ("Real-Time Vacancy Aggregation", "Aggregates vacancies across all active courses dynamically."),
                ("Quick Action Hub", "One-click shortcuts to Manage Courses, View Students, Faculty, and Audit Logs."),
                ("Live Synchronized Sync", "Telemetry recalibrates automatically whenever switching tabs.")
            ]
        ),
        (
            11,
            "Course CRUD & Safe Deletion Guard",
            "TOUR • 07 • COURSE ADMINISTRATION",
            "Full course lifecycle management with enterprise data safety validation.",
            "screenshots/07_admin_manage_courses.png",
            "AdminCoursePanel.java — Course Management",
            "Course Administration Highlights",
            [
                ("Full CRUD Operations", "Administrators can add new offerings, edit parameters, or delete courses."),
                ("Input Form Validation", "Modal forms validate non-empty fields, positive credits, and capacity thresholds."),
                ("Safe Deletion Guard", "Blocks course deletion if registeredStudents > 0, preventing orphaned student records."),
                ("3-Way Collection Sync", "Synchronously updates Array, HashMap, and TreeMap collections.")
            ]
        ),
        (
            12,
            "Faculty Management System",
            "TOUR • 08 • INSTRUCTOR DIRECTORY",
            "Instructor directory fulfilling Problem Statement requirement to manage faculty.",
            "screenshots/09_admin_faculty.png",
            "AdminFacultyPanel.java — Faculty Directory",
            "Faculty Portal Highlights",
            [
                ("Syllabus Requirement", "Specifically satisfies the Problem Statement mandate to manage faculty records."),
                ("Faculty Directory", "Displays Faculty ID, Name, Department (CSE/IT), and institutional email address."),
                ("Live Search Bar", "Real-time filtering across ID, Name, Department, or Email."),
                ("'+ Add Faculty' Modal", "Registers new professors into DataStore.faculties with duplicate ID validation.")
            ]
        ),
        (
            13,
            "System-Wide Audit Log & Transactions",
            "TOUR • 09 • AUDIT LOG",
            "Chronological ledger tracking all student registrations and cancellations.",
            "screenshots/10_admin_registrations.png",
            "AdminRegistrationPanel.java — Audit Trail",
            "Audit Trail Highlights",
            [
                ("Audit Trail Integrity", "Tracks Registration ID, Student ID, Student Name, Course, and Timestamp."),
                ("Status Badges", "Green 'REGISTERED' badge for active enrollments and red 'CANCELLED' for history."),
                ("History Preservation", "Cancellations are never hard-deleted; status transitions preserve complete history."),
                ("Powered by LinkedList", "Demonstrates chronological sequential traversal of LinkedList<Registration>.")
            ]
        )
    ]

    for slide_num, title, category, subtitle, img_path, win_title, card_heading, feats in showcases:
        s = prs.slides.add_slide(blank_layout)
        apply_slide_bg(s)
        add_slide_header(s, title, category, subtitle)

        # Left: Handcrafted macOS Window Mockup
        draw_window_mockup(s, Inches(0.8), Inches(1.75), Inches(7.7), Inches(5.2), win_title, img_path)

        # Right: Clean Feature Card
        fcard = s.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(8.75), Inches(1.75), Inches(3.78), Inches(5.2))
        fcard.fill.solid()
        fcard.fill.fore_color.rgb = CARD_BG
        fcard.line.color.rgb = CARD_BORDER
        fcard.line.width = Pt(1)

        ftf = fcard.text_frame
        ftf.word_wrap = True
        ftf.margin_left = Inches(0.3)
        ftf.margin_right = Inches(0.3)
        ftf.margin_top = Inches(0.35)

        fp_pill = ftf.paragraphs[0]
        fp_pill.text = "[ ARCHITECTURAL HIGHLIGHTS ]"
        fp_pill.font.name = FONT_DISPLAY
        fp_pill.font.size = Pt(8.5)
        fp_pill.font.bold = True
        fp_pill.font.color.rgb = BRAND_LIGHT

        fp_head = ftf.add_paragraph()
        fp_head.text = card_heading
        fp_head.font.name = FONT_DISPLAY
        fp_head.font.size = Pt(15)
        fp_head.font.bold = True
        fp_head.font.color.rgb = TEXT_HEADING
        fp_head.space_before = Pt(4)

        for feat_title, feat_desc in feats:
            fp_item = ftf.add_paragraph()
            fp_item.text = f"•  {feat_title}: {feat_desc}"
            fp_item.font.name = FONT_BODY
            fp_item.font.size = Pt(10.5)
            fp_item.font.color.rgb = TEXT_BODY
            fp_item.space_before = Pt(8)

    # ==========================================
    # SLIDE 14: Team Members & Contributions
    # ==========================================
    s14 = prs.slides.add_slide(blank_layout)
    apply_slide_bg(s14)
    add_slide_header(s14, "Team Members & Contributions", "GROUP 11 • INDIVIDUAL OWNERSHIP", "Clear breakdown of individual domain responsibilities, deliverables, and architecture.")

    team_members = [
        (
            "Jaikishan",
            "AUTH & STUDENT PORTAL",
            BRAND_LIGHT,
            [
                ("Student.java & StudentService.java", "Core student entity and authentication."),
                ("LoginFrame.java", "Split-screen GUI with Enter-key submission."),
                ("StudentDashboard.java", "Responsive CardLayout with live tab auto-refresh."),
                ("StudentHomePanel.java", "4 live metric cards and recent course audit.")
            ]
        ),
        (
            "Omkar",
            "COURSE DOMAIN & SAFE CRUD",
            ACCENT_EMERALD,
            [
                ("Course.java & CourseService.java", "Core course entity and capacity rules."),
                ("System.arraycopy Resizing", "Doubles Course[] capacity on demand."),
                ("CoursePanel.java", "Live search and Available/Full status badges."),
                ("AdminCoursePanel.java", "Add/Edit modals and Safe Deletion Guard.")
            ]
        ),
        (
            "Sarthak",
            "REGISTRATION & CREDIT CAP",
            ACCENT_CYAN,
            [
                ("Registration.java & Service", "Enrollment, cancellation, and validation."),
                ("18-Credit Semester Cap", "Hard rule preventing student credit overload."),
                ("RegistrationPanel.java", "Dynamic course selection with info card."),
                ("MyCoursesPanel.java", "Interactive table with red Cancel button editor.")
            ]
        ),
        (
            "Danish",
            "DATA & ADMIN SYSTEMS",
            ACCENT_AMBER,
            [
                ("DataStore.java", "Centralized multi-collection data persistence."),
                ("Faculty Management", "Faculty entity, service, and AdminFacultyPanel."),
                ("AdminDashboard.java", "Executive overview with 5 real-time metrics."),
                ("AdminRegistrationPanel", "LinkedList-powered audit trail viewer.")
            ]
        )
    ]

    for idx, (name, role, tag_col, deliverables) in enumerate(team_members):
        tx = Inches(0.8 + idx * 2.98)
        tcard = s14.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, tx, Inches(1.8), Inches(2.78), Inches(5.1))
        tcard.fill.solid()
        tcard.fill.fore_color.rgb = CARD_BG
        tcard.line.color.rgb = CARD_BORDER
        tcard.line.width = Pt(1)

        ttf = tcard.text_frame
        ttf.word_wrap = True
        ttf.margin_left = Inches(0.2)
        ttf.margin_right = Inches(0.2)
        ttf.margin_top = Inches(0.25)

        tp_tag = ttf.paragraphs[0]
        tp_tag.text = f"[ {role} ]"
        tp_tag.font.name = FONT_DISPLAY
        tp_tag.font.size = Pt(8)
        tp_tag.font.bold = True
        tp_tag.font.color.rgb = tag_col

        tp_name = ttf.add_paragraph()
        tp_name.text = name
        tp_name.font.name = FONT_DISPLAY
        tp_name.font.size = Pt(16)
        tp_name.font.bold = True
        tp_name.font.color.rgb = TEXT_HEADING
        tp_name.space_before = Pt(3)

        tp_sec = ttf.add_paragraph()
        tp_sec.text = "Key Deliverables:"
        tp_sec.font.name = FONT_DISPLAY
        tp_sec.font.size = Pt(10)
        tp_sec.font.bold = True
        tp_sec.font.color.rgb = TEXT_MUTED
        tp_sec.space_before = Pt(10)

        for d_file, d_desc in deliverables:
            dp_item = ttf.add_paragraph()
            dp_item.text = f"•  {d_file}: {d_desc}"
            dp_item.font.name = FONT_BODY
            dp_item.font.size = Pt(9.5)
            dp_item.font.color.rgb = TEXT_BODY
            dp_item.space_before = Pt(5)

    # ==========================================
    # SLIDE 15: Conclusion & Future Scope
    # ==========================================
    s15 = prs.slides.add_slide(blank_layout)
    apply_slide_bg(s15)

    pill15 = s15.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(0.7), Inches(3.6), Inches(0.36))
    pill15.fill.solid()
    pill15.fill.fore_color.rgb = RGBColor(28, 38, 59)
    pill15.line.color.rgb = BRAND_INDIGO
    pill15.line.width = Pt(1.2)
    ptf15 = pill15.text_frame
    ptf15.vertical_anchor = MSO_ANCHOR.MIDDLE
    p15 = ptf15.paragraphs[0]
    p15.text = "CONCLUSION & FUTURE HORIZONS"
    p15.font.name = FONT_DISPLAY
    p15.font.size = Pt(10)
    p15.font.bold = True
    p15.font.color.rgb = BRAND_LIGHT
    p15.alignment = PP_ALIGN.CENTER

    tb15_title = s15.shapes.add_textbox(Inches(0.8), Inches(1.15), Inches(11.733), Inches(1.4))
    ttf15 = tb15_title.text_frame
    ttf15.word_wrap = True
    ttf15.margin_left = 0
    ttf15.margin_top = 0

    p_cmain = ttf15.paragraphs[0]
    p_cmain.text = "Enterprise-Grade Academic Architecture"
    p_cmain.font.name = FONT_DISPLAY
    p_cmain.font.size = Pt(32)
    p_cmain.font.bold = True
    p_cmain.font.color.rgb = TEXT_HEADING

    p_cdesc = ttf15.add_paragraph()
    p_cdesc.text = "Successfully demonstrates Object-Oriented Design, Java Swing thread safety, and Collection Framework mastery while fulfilling all 8 syllabus objectives."
    p_cdesc.font.name = FONT_BODY
    p_cdesc.font.size = Pt(13)
    p_cdesc.font.color.rgb = TEXT_BODY
    p_cdesc.space_before = Pt(6)

    # 2 Comparison Split Cards
    # Left: Key Achievements
    ca1 = s15.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(2.75), Inches(5.6), Inches(3.2))
    ca1.fill.solid()
    ca1.fill.fore_color.rgb = CARD_BG
    ca1.line.color.rgb = CARD_BORDER
    ca1.line.width = Pt(1)

    catf1 = ca1.text_frame
    catf1.word_wrap = True
    catf1.margin_left = Inches(0.35)
    catf1.margin_right = Inches(0.35)
    catf1.margin_top = Inches(0.3)

    p_atitle = catf1.paragraphs[0]
    p_atitle.text = "Key Engineering Achievements"
    p_atitle.font.name = FONT_DISPLAY
    p_atitle.font.size = Pt(15)
    p_atitle.font.bold = True
    p_atitle.font.color.rgb = BRAND_LIGHT

    achievements = [
        "100% compliance with Mini Project 11 syllabus specifications.",
        "Zero external database dependencies with fast in-memory execution.",
        "Enterprise safeguards: Safe Deletion, 18-Credit Cap, and Tab Auto-Sync.",
        "Dual-portal UI with accessible keyboard shortcuts and responsive CardLayout."
    ]
    for ach in achievements:
        p_item = catf1.add_paragraph()
        p_item.text = f"✓  {ach}"
        p_item.font.name = FONT_BODY
        p_item.font.size = Pt(11)
        p_item.font.color.rgb = TEXT_BODY
        p_item.space_before = Pt(6)

    # Right: Future Scope
    ca2 = s15.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(6.8), Inches(2.75), Inches(5.733), Inches(3.2))
    ca2.fill.solid()
    ca2.fill.fore_color.rgb = CARD_BG
    ca2.line.color.rgb = CARD_BORDER
    ca2.line.width = Pt(1)

    catf2 = ca2.text_frame
    catf2.word_wrap = True
    catf2.margin_left = Inches(0.35)
    catf2.margin_right = Inches(0.35)
    catf2.margin_top = Inches(0.3)

    p_ftitle = catf2.paragraphs[0]
    p_ftitle.text = "Future Engineering Scope"
    p_ftitle.font.name = FONT_DISPLAY
    p_ftitle.font.size = Pt(15)
    p_ftitle.font.bold = True
    p_ftitle.font.color.rgb = ACCENT_EMERALD

    futures = [
        "Persistent SQL Database integration (MySQL / PostgreSQL via JDBC).",
        "Automated Course Waitlisting with FIFO queue seat handoff.",
        "Automated PDF Course Schedule generation using iText or Apache PDFBox.",
        "Role-based multi-factor authentication (MFA) and institutional LDAP/SSO."
    ]
    for fut in futures:
        p_item = catf2.add_paragraph()
        p_item.text = f"›  {fut}"
        p_item.font.name = FONT_BODY
        p_item.font.size = Pt(11)
        p_item.font.color.rgb = TEXT_BODY
        p_item.space_before = Pt(6)

    # Bottom Thank You & Q&A Strip
    ty_box = s15.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(6.15), Inches(11.733), Inches(0.85))
    ty_box.fill.solid()
    ty_box.fill.fore_color.rgb = RGBColor(16, 23, 36)
    ty_box.line.color.rgb = BRAND_INDIGO
    ty_box.line.width = Pt(1)

    tytf = ty_box.text_frame
    tytf.word_wrap = True
    tytf.vertical_anchor = MSO_ANCHOR.MIDDLE
    
    tp_ty = tytf.paragraphs[0]
    tp_ty.text = "Thank You! Any Questions?"
    tp_ty.font.name = FONT_DISPLAY
    tp_ty.font.size = Pt(15)
    tp_ty.font.bold = True
    tp_ty.font.color.rgb = TEXT_HEADING
    tp_ty.alignment = PP_ALIGN.CENTER

    tp_sub = tytf.add_paragraph()
    tp_sub.text = "Group 11 • Cohort: Jeff Bezos • Batch: 2025 – 2029 • Mini Project #11"
    tp_sub.font.name = FONT_DISPLAY
    tp_sub.font.size = Pt(10)
    tp_sub.font.color.rgb = TEXT_MUTED
    tp_sub.alignment = PP_ALIGN.CENTER
    tp_sub.space_before = Pt(2)

    # Save generated PPTX
    out_file = "College_Course_Registration_Presentation_Group11.pptx"
    prs.save(out_file)
    print(f"SUCCESS: Generated human-designed presentation at {out_file}")

if __name__ == "__main__":
    build_presentation()
