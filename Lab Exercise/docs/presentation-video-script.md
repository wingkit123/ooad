# Hospital Management System Video Presentation Script

**Total Duration:** ~15 Minutes
**Target Audience:** Grading Tutors
**Setup:** Have the Java IDE / Terminal open, compile the code beforehand, and have the UML diagrams ready on-screen.

---

## 🎬 Video Overview & Timing Checklist

| Section | Topic | Timing | Presenter (Example) |
| :--- | :--- | :--- | :--- |
| **Section 1** | **Introduction** | **0:00 - 1:00 (1 min)** | Presenter A / All |
| **Section 2** | **Class Diagram** | **1:00 - 4:00 (3 mins)** | Presenter B |
| **Section 3** | **Sequence Diagrams** | **4:00 - 7:00 (3 mins)** | Presenter C |
| **Section 4** | **System Demonstration** | **7:00 - 13:00 (6 mins)** | Presenter D / A |
| **Section 5** | **Code Explanation** | **13:00 - 15:00 (2 mins)** | Presenter E / All |

---

## 📁 Section 1: Introduction (0:00 - 1:00)

**[Screen Action]:** *Show a title slide with "Hospital Management System (HMS) MVP" and the names/IDs of all group members.*

**[Presenter A]:**
> "Hello everyone, and welcome to our presentation of the Hospital Management System MVP. Our team consists of:
> * **[Member 1 Name]** - Project Manager & Core Services Developer
> * **[Member 2 Name]** - Lead Architect & UML Designer
> * **[Member 3 Name]** - UI/UX Designer & Swing Developer
> * **[Member 4 Name]** - Quality Assurance & Code Analyst
>
> Today, we will show you our Java Swing desktop implementation. The project is designed as a lightweight, clean, and highly robust system built around a solid Object-Oriented paradigm. 
> 
> Our system covers core modules: role-based login, patient records, doctor management, appointment booking with conflict prevention, and system reporting.
> 
> Let's start by walking through our architectural blueprint."

---

## 📁 Section 2: Class Diagram Explanation Guide (1:00 - 4:00)

**[Screen Action]:** *Open `uml/class-diagram.puml` image on screen. Zoom in so text is legible. Use your mouse cursor to point to specific classes as you mention them.*

**[Presenter B - Speaking & Pointing Guide]:**
1. **Explain the Domain Models (Point to the top left area: `User`, `Role`, `AdminUser`, etc.)**
   > "Here is our Class Diagram. Let's start with our Domain Models. 
   > *(Point to `User`)* At the top, we define an abstract class `User` representing system users. It contains standard attributes like `username` and `password`.
   > *(Point to `AdminUser`, `DoctorUser`, `ReceptionistUser`)* We utilize **Inheritance** here: `AdminUser`, `DoctorUser`, and `ReceptionistUser` extend `User`.
   > *(Point to `Appointment`, `Patient`, `Doctor`)* An `Appointment` maintains a strong composition with exactly one `Patient` and one `Doctor`."
2. **Explain the Architecture Facade (Point to the center: `SystemController`)**
   > *(Hover mouse over `SystemController`)* "Our GUI components do not interact directly with logic. Instead, they interact with `SystemController`, which acts as a **Facade** or Abstraction layer. The `SystemController` orchestrates different services, keeping the UI clean."
3. **Explain the Services (Point to the bottom area: `PatientService`, `AppointmentService`, etc.)**
   > *(Circle mouse around the Service classes)* "We have dedicated service classes. This modularity enforces the **Single Responsibility Principle**. For example, the `AppointmentService` is strictly responsible for appointment logic."
4. **Explain the Data Storage (Point to `HospitalRepository`)**
   > *(Point to `HospitalRepository`)* "Finally, to keep the system lightweight without database setup overhead, we implemented a centralized in-memory repository (`HospitalRepository`) containing list-backed data for Users, Patients, Doctors, and Appointments."

---

## 📁 Section 3: Sequence Diagrams Explanation Guide (4:00 - 7:00)

**[Screen Action]:** *Switch between the 4 sequence diagrams one-by-one. Use your mouse to trace the arrows from left (Actor) to right (Repository) and back.*

**[Presenter C - Speaking & Pointing Guide]:**

**1. Login Sequence (`sequence-login.puml`)**
   > *(Open image. Trace the arrow from User to LoginFrame)* "In the Login process, the User enters credentials in the `LoginFrame`. 
   > *(Trace arrow to SystemController -> AuthenticationService)* The frame calls `SystemController.login()`, which delegates authentication to `AuthenticationService`.
   > *(Trace back to LoginFrame)* If successful, the GUI opens the correct dashboard layout based on the user's role."

**2. Appointment Booking (`sequence-book-appointment.puml`)**
   > *(Open image. Emphasize the 'alt' block in the middle)* "For Appointment Booking, the user selects details in the UI. 
   > *(Point to `AppointmentService -> HospitalRepository`)* The `AppointmentService` checks for conflicting schedules.
   > *(Point to the `alt duplicate doctor slot exists` box)* As you can see in this 'alt' block, if a duplicate is found, it immediately throws an Exception back to the UI. Otherwise, it retrieves the Patient and Doctor objects and saves the Appointment."

**3. Patient Management (`sequence-add-patient.puml`)**
   > *(Open image. Trace arrow to `PatientService`)* "Adding a patient is straightforward: The UI calls `SystemController`, which invokes `PatientService`.
   > *(Point to `HospitalRepository` creating Patient)* The service registers a new `Patient` object with the `HospitalRepository` using an auto-incremented ID, and the UI refreshes the table."

**4. Report Generation (`sequence-generate-report.puml`)**
   > *(Open image. Point to `ReportService`)* "When generating a report, the `SystemController` invokes `ReportService`.
   > *(Trace arrows from `ReportService` to `HospitalRepository`)* The service queries both Patient and Appointment lists from the `HospitalRepository`.
   > *(Point to `ReportService -> ReportService: count totals`)* It then counts totals and builds schedules internally, returning a consolidated `ReportSummary` object to the GUI."

---

## 📁 Section 4: System Demonstration (7:00 - 13:00)

**[Screen Action]:** *Run the Swing application live. Perform the actions step-by-step. Speak clearly as you click.*

**[Presenter D / Live Demo Operator]:**
> **Step 1: Role-Based Login & Header Options**
> *(Type `reception` / `reception123`)* "First, let's log in as a Receptionist. Notice that we now have a global **'Sign Out'** button at the top-right header, letting us return to the login screen. Also, in the Doctors tab, all action buttons ('Add', 'Update', 'Delete') are disabled, and input fields are read-only because Receptionists have read-only access to doctor schedules.
> *(Click Sign Out -> Log in as `admin` / `admin123`)* Let's log in as the Administrator. The Admin dashboard gives us full editing privileges across all tabs."
> 
> **Step 2: Add Patient & Strict Input Validation**
> *(Click 'Patients' tab -> Type 'Alice Green' in Name, 'abc' in Age -> Click 'Add Patient')* "We implemented strict real-time validations. For example, if we type letters like 'abc' in the Age field, the system pops up a warning: 'Patient age must be a valid positive integer'. If we try to input numbers in the Name field, the system blocks it. 
> *(Type valid details: Alice Green, 30, Female -> Click 'Add Patient')* When entering correct data, the patient is successfully added."
> 
> **Step 3: Edit Patient & Clean Layout**
> *(Tick Alice Green's checkbox)* "Notice the new layout: We organized the inputs in Row 1 and the action buttons in Row 2 to ensure they never get cut off on smaller screens. Ticking exactly 1 patient instantly loads their details in the fields and shows the **'Update Patient'** button, letting us edit details. Unchecking them clears the fields."
> 
> **Step 4: Doctor Management & Bulk Status Updates**
> *(Click 'Doctors' tab -> Check 2 doctors)* "In the Doctors tab, we added checkbox columns with compact sizes. If we check **more than 1** doctor, the Name and Specialization fields immediately disappear from the screen, leaving only the Status dropdown visible. We can set it to 'Busy' and click **'Update Doctor'** to batch-update their availability status at the same time."
> 
> **Step 5: Book Appointment & Doctor Availability Validation**
> *(Click 'Appointments' tab)* "When booking an appointment, the system checks the doctor's status. If we select a doctor whose status is 'Busy' or 'On Leave', the booking will be rejected. This ensures scheduling is always valid."
> 
> **Step 6: One-Action Bulk Deletions**
> *(Tick 2 patients -> Click 'Delete Patient')* "Finally, Admins can perform bulk deletions in one action. Deleting patients also cascades and deletes all their associated appointments automatically, maintaining repository integrity."

---

## 📁 Section 5: Code Explanation Guide (13:00 - 15:00)

**[Screen Action]:** *Open the IDE. Switch between the mentioned `.java` files. Use your mouse to highlight the specific lines of code being discussed.*

**[Presenter E - Speaking & Pointing Guide]:**

1. **Inheritance & Polymorphism (`hms/model/User.java`)**
   > *(Open `User.java` -> highlight `public abstract class User`)* "To demonstrate our Object-Oriented design, let's look at the `User` class. It is `abstract`. 
   > *(Open `AdminUser.java` -> highlight `extends User` and `@Override public String getHomeMessage()`)* Subclasses like `AdminUser` extend it and override `getHomeMessage()`. This demonstrates **Inheritance** and **Polymorphism**."

2. **Encapsulation & Validation (`hms/model/Patient.java`)**
   > *(Open `Patient.java` -> highlight `private String name;`, etc.)* "In the `Patient` class, member variables are strictly declared `private`. 
   > *(Highlight the regex matches `name.matches(".*\\d.*")`)* We added regex-based parameter validation to the `update()` method to block numbers in names and invalid inputs, demonstrating robust **Encapsulation**."

3. **Abstraction & Cascade Operations (`hms/service/HospitalRepository.java`)**
   > *(Open `HospitalRepository.java` -> highlight `deletePatient` and `deleteDoctor`)* "Our repository implements cascade deletes (`deletePatient(id)` and `deleteDoctor(id)`) to remove linked appointments. By hiding this implementation from the GUI and exposing it via the Facade Controller, we fulfill the principle of **Abstraction**."

4. **Business Rules & State Checking (`hms/service/AppointmentService.java`)**
   > *(Open `AppointmentService.java` -> highlight doctor availability checks)* "Finally, in `AppointmentService.java`, the system validates the doctor's `status` ('Available' vs 'Busy' / 'On Leave') before scheduling, throwing custom exceptions to protect business workflows."
   > 
> "This completes our presentation. Thank you for your time!"
