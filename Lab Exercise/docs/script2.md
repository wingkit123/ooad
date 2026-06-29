# Hospital Management System (HMS) - Official Video Presentation Script

This script strictly follows the 5-section sequence required by Section 7 of the assignment rubric to ensure maximum grading alignment. Phrasing is optimized for clear pronunciation by native Chinese speakers, featuring screen-action markers and explicit context tags `[中文提示]`.

---

## 🕒 Video Timing & Presenter Rotation Sheet

| Rubric Section | Presentation Topic | Target Timing | Primary Speaker |
| :--- | :--- | :--- | :--- |
| **Section 1** | **Introduction & System Overview** | 0:00 - 1:00 (1 min) | Presenter 1 |
| **Section 2** | **Class Diagram Design & Relationships** | 1:00 - 4:00 (3 mins) | Presenter 1 & **Presenter 2 (Wing Kit)** |
| **Section 3** | **Sequence Diagrams Workflow Analysis** | 4:00 - 7:00 (3 mins) | **Presenter 2 (Wing Kit)** & Presenter 3 |
| **Section 4** | **Live System Functional Demonstration** | 7:00 - 13:00 (6 mins) | Presenter 3 & Presenter 4 |
| **Section 5** | **Code Architecture & OOP Implementation** | 13:00 - 15:00 (2 mins) | Presenter 4 |

---

## 🎬 Full Production Script

### 🎬 Section 1: Introduction (0:00 - 1:00)
**[Screen Action]:** *Show Title Slide containing "Hospital Management System (HMS) MVP", Group Section, and all 4 Student Names/IDs clearly listed.*

**Presenter 1:**
> "Hello everyone, and welcome to our project presentation. Our team has designed and implemented a standalone **Hospital Management System MVP** using Java Swing. Before we begin, let introduce our group members:
> * **Presenter 1:** [Name & ID] - Project Coordinator & View Developer
> * **Presenter 2 (Wing Kit):** See Wing Kit [& ID] - Lead Architect & Core Logic Developer
> * **Presenter 3:** [Name & ID] - Workflow Modeler & QA Analyst
> * **Presenter 4:** [Name & ID] - System Integrator & Code Reviewer
> 
> *Visual Cue: Switch to Slide 2 - System Module Map*
> 
> Our application is divided into five core operational modules: Secure Role-Based Authentication, Patient Record Tracking, Doctor Scheduling, Conflict-Free Appointment Booking, and Aggregated Reporting. The architecture strictly separates data definitions from business validation layers to achieve high system maintainability.
> 
> Now, let's open our structural blueprints and examine Section 2: the Class Diagram."

---

### 🎬 Section 2: Class Diagram (1:00 - 4:00)
**[Screen Action]:** *Switch on-screen image to `uml/class-diagram.puml`. Zoom in cleanly onto the upper section containing the User class hierarchy.*

**Presenter 1:**
> "For our static structure design, we created a centralized object model mapped across specific domain packages. 
> 
> *(Point mouse cursor to the top `User` box)* At the foundation of our access control model, we have an abstract base class `User`. This class encapsulates shared account credentials like `username`, `password`, and `displayName`. To support distinct corporate levels inside a hospital, we use an Enum class called `Role`, which contains `ADMIN`, `DOCTOR`, and `RECEPTIONIST`.
> 
> I will now pass the floor to Wing Kit to explain our structural inheritance and design justifications."

**Presenter 2 (Wing Kit):**
> "Thank you, Presenter 1. Let's look closer at our structural hierarchy and class relationships.
> 
> *(Hover mouse cursor over the inheritance lines pointing down to `AdminUser`, `DoctorUser`, and `ReceptionistUser`)* We implemented the principle of **Inheritance** `[中文提示: 继承]` by extending our base `User` class into three concrete sub-classes. Each subclass overrides the polymorphic method `getHomeMessage()` `[中文提示: 多态方法]` to deliver role-specific dashboards back to the graphical user interface.
> 
> *Visual Cue: Move mouse cursor to point to `Appointment`, `Patient`, and `Doctor` boxes*
> 
> *(Point to `Appointment` connections)* An `Appointment` object maintains a clear **Composition** relationship `[中文提示: 组合关系]` with exactly one `Patient` and one `Doctor` instance. It contains specific fields for tracking `LocalDate date` and `LocalTime time` to model real-world clinical entries.
> 
> *Visual Cue: Hover mouse around the Service blocks (`PatientService`, `AppointmentService`, etc.) and `SystemController`*
> 
> We strongly justify this class structure through the **Facade Design Pattern** `[中文提示: 外观设计模式]`. Our Swing view frames never communicate directly with domain components. Instead, they communicate exclusively through a single gateway class called `SystemController`. This controller acts as our system facade, delegating actions out to highly specialized services like `PatientService` or `AppointmentService`. This keeps our system decoupled and easily expandable for future extensions.
> 
> Next, let's look at Section 3 to see our Sequence Diagrams."

---

### 🎬 Section 3: Sequence Diagrams (4:00 - 7:00)
**[Screen Action]:** *Switch on-screen view to `uml/sequence-login.puml`. Trace line movements horizontally from left to right as you present.*

**Presenter 2 (Wing Kit):**
> "In Section 3, we analyze how our decoupled objects interact dynamically at runtime. 
> 
> Let's look at the **Login Process workflow**. When a user types their details inside our `LoginFrame`, the view triggers the operation by passing arguments into `SystemController.login()`. The controller delegates authentication to our `AuthenticationService`. The service loops through the user collection loaded from our central database facade, `HospitalRepository`. If the username and password attempt match perfectly, the controller safely opens the role-based `DashboardFrame`.
> 
> *Visual Cue: Switch image file to `uml/sequence-add-patient.puml`*
> 
> Next is our **Record Management flow**, specifically adding a patient. When a receptionist submits registration fields, the request travels through `SystemController` into `PatientService`. The service calls `HospitalRepository.addPatient()`, which encapsulates an auto-increment identity counter `[中文提示: 自增主键计数器]` to generate a distinct ID. The newly instantiated `Patient` reference is returned back up to refresh our visual table elements.
> 
> Presenter 3 will now take over to present our complex appointment scheduling sequence."

**Presenter 3:**
> "Thank you, Wing Kit. Let's move to our most critical validation pathway: the **Appointment Booking Flow**.
> 
> *Visual Cue: Open `uml/sequence-book-appointment.puml` and point specifically to the large `alt` conditional block*
> 
> When a booking request is made, the `AppointmentService` fetches all current bookings from the repository to verify scheduling conflicts. As explicitly mapped inside this conditional **Alternative Block** `[中文提示: 选择分支组合碎片]`, if a duplicate slot matches the same doctor, date, and time, the execution branch throws a `DuplicateAppointmentException` back to the view. This completely stops the transaction and prompts a pop-up alert on the screen. Only when the slot is confirmed available does the system save the new appointment record.
> 
> Let's move on to Section 4 and watch a live system demonstration."

---

### 🎬 Section 4: System Demonstration (7:00 - 13:00)
**[Screen Action]:** *Launch the live Java Swing application by running `java -cp hms-swing/out hms.app.HospitalManagementApp` via terminal. Keep the interface centered and clearly visible on screen.*

**Presenter 3:**
> "Now, we will demonstrate our operational system interface built entirely on Java Swing.
> 
> **Step 1: Role-Based Authentication**
> First, let's type the credentials `reception` and `reception123` to log in as a receptionist. Notice that the header instantly displays a custom welcome message showing our name and assigned role. If we click on the Doctors tab, you will notice that all management inputs are disabled and action buttons are greyed out, because receptionists only have read-only access to doctor files.
> 
> *Visual Cue: Click 'Sign Out' at the top-right -> Re-login using 'admin' and 'admin123'*
> 
> Let's sign out and log back in using our Admin account. As an administrator, our dashboard unlocks full management controls across all modules.
> 
> **Step 2: Add Patient Record with Validation**
> Let's move to the Patients tab and demonstrate our strict input validation. If I try to input characters like 'abc' inside our Age field and click add, our controller catches the error and blocks the transaction, showing a clear warning popup: 'Patient age must be a valid positive integer'. Now, let's enter valid information: Name as 'Alice Green', Age as '30', Gender as 'Female', and click **Add Patient**. The table instantly updates with our new patient entry.
> 
> Presenter 4 will now complete the appointment scheduling and reporting demonstration."

**Presenter 4:**
> "Thank you, Presenter 3. Let's complete our clinical workflow demonstration.
> 
> **Step 3: Conflict-Free Appointment Booking**
> Let's switch over to the Appointments tab. The patient dropdown automatically displays our newly registered patient, 'Alice Green', and our seeded medical specialist, 'Dr. Kumar'. Let's book an appointment for date '2026-06-20' at time '09:00' and click **Book Appointment**. The booking completes successfully.
> 
> *Visual Cue: Keep the exact same Doctor, Date, and Time values -> Click 'Book Appointment' again*
> 
> Now, let's try to book the exact same doctor at the exact same date and time slot again. When I click the button, our backend validation throws our custom exception, blocking the double-booking and displaying a clear warning popup: 'Doctor already has an appointment at this date and time'. This demonstrates excellent business state protection.
> 
> **Step 4: Real-time Report Generation**
> Finally, let's move to our Reports tab and click **Generate Report**. The module fetches data from our repository layer and displays live hospital summaries, including our total patient count, total active appointments, and organized daily doctor schedules.
> 
> Let's move to our final section: Section 5 for the Code Explanation."

---

### 🎬 Section 5: Code Explanation (13:00 - 15:00)
**[Screen Action]:** *Open your Integrated Development Environment (IDE) or code editor. Bring up the source files in tab views and highlight lines using your cursor as they are mentioned.*

**Presenter 4:**
> "To wrap up our presentation, we will review how our source code implements core Object-Oriented Programming principles.
> 
> *Visual Cue: Open `hms/model/User.java` in the editor*
> 
> **1. Abstraction and Inheritance:**
> Look at our `User` class definition. It is declared as an `abstract class`. It defines our structural blueprint while protecting constructor access via the `protected` modifier. Subclasses like `AdminUser.java` or `DoctorUser.java` extend this base structure, showcasing clean **Inheritance**.
> 
> *Visual Cue: Open `hms/model/Patient.java` in the editor*
> 
> **2. Strict Encapsulation and Data Defense:**
> Inside our `Patient` model class, all core variables like `name`, `age`, and `gender` are strictly declared as `private` fields. Data can only be read through explicit getters, and modification is locked behind our validated `update()` method. We implemented regular expression pattern matching `name.matches(".*\\d.*")` directly inside our setters to block invalid data numbers from corrupting patient names.
> 
> *Visual Cue: Open `hms/service/AppointmentService.java`*
> 
> **3. Business Modularity:**
> Finally, our `AppointmentService` coordinates our core scheduling constraints. It validates the doctor's status field (`"Available"` vs `"Busy"` or `"On Leave"`) before booking, protecting system integrity from the business layer rather than relying on UI controls. This decoupled framework ensures that our system is highly maintainable, scalable, and fully future-proof for additional hospital modules.
> 
> This marks the end of our presentation. Thank you very much for your time!"